package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.database.post.SubjectRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.post.Subject
import com.github.sdp.ratemyepfl.model.post.SubjectKind
import com.github.sdp.ratemyepfl.model.time.DateTime
import com.github.sdp.ratemyepfl.viewmodel.AddPostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSubjectViewModel @Inject constructor(
    private val subjectRepo: SubjectRepository,
    private val connectedUser: ConnectedUser,
) : AddPostViewModel<Subject>() {

    companion object {
        const val NO_KIND_MESSAGE: String = "You need to give a kind!"
    }

    private val kind: MutableLiveData<SubjectKind> = MutableLiveData(null)

    /**
     * Set the kind chosen by the user
     * @param kind: kind chosen by the user
     */
    fun setKind(kind: SubjectKind?) {
        this.kind.postValue(kind)
    }

    /**
     * Builds and submits the subject to the database
     *
     * @throws IllegalStateException if the user is not connected, or if one of the fields is empty
     */
    fun submitSubject() {
        val kind = kind.value
        val comment = comment.value
        val title = title.value
        val date = DateTime.now()
        var uid: String? = null

        // only connected users may add reviews
        if (!connectedUser.isLoggedIn()) {
            throw DisconnectedUserException()
        } else if (title.isNullOrEmpty()) {
            throw MissingInputException(EMPTY_TITLE_MESSAGE)
        } else if (kind == null) {
            throw MissingInputException(NO_KIND_MESSAGE)
        }

        if (!anonymous.value!!) {
            uid = connectedUser.getUserId()
        }

        val builder = Subject.Builder()
            .setKind(kind)
            .setTitle(title)
            .setComment(comment)
            .setDate(date)
            .setUid(uid)
        try {
            val subject = builder.build()
            viewModelScope.launch(Dispatchers.IO) {
                subjectRepo.add(subject).collect()
            }
        } catch (e: IllegalStateException) {
            throw IllegalStateException("Failed to build the subject (from ${e.message}")
        }
    }
}