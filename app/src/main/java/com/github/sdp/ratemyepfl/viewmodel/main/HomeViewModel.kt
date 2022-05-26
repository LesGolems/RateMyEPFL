package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository

import com.github.sdp.ratemyepfl.backend.database.post.SubjectRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.VoteException
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.model.review.SubjectWithAuthor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val subjectRepo: SubjectRepository,
    private val userRepo: UserRepository,
    private val imageStorage: Storage<ImageFile>
) : ViewModel() {

    // Subjects
    val subjects = MutableLiveData<List<SubjectWithAuthor>>()

    @Inject
    lateinit var auth: ConnectedUser

    init {
        updateSubjectsList()
    }

    fun updateSubjectsList() {
        viewModelScope.launch {
            subjects.postValue(subjectRepo.getSubjects()
                .toMutableList()
                .map { subject ->
                    PostWithAuthor(
                        subject,
                        subject.uid?.let { userRepo.getUserByUid(it) },
                        subject.uid?.let { imageStorage.get(it).lastOrNull() }
                    )
                }
                .sortedBy { rwa -> -rwa.post.likers.size })
        }
    }

    fun removeSubject(subjectId: String) {
        viewModelScope.launch {
            subjectRepo.remove(subjectId).await()
            updateSubjectsList()
        }
    }

    fun updateDownVotes(subject: Subject, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't dislike your own post")
        val subjectId = subject.getId()

        viewModelScope.launch {
            // The user already disliked the review
            if (subject.dislikers.contains(uid)) {
                // Remove a dislike
                subjectRepo.removeDownVote(subjectId, uid)
                userRepo.updateKarma(authorUid, 1)
            } else {
                // The user dislikes for the first time
                if (subject.likers.contains(uid)) {
                    // The user changed from like to dislike
                    subjectRepo.removeUpVote(subjectId, uid)
                    userRepo.updateKarma(authorUid, -1)
                }
                // Add a dislike
                subjectRepo.addDownVote(subject.getId(), uid)
                userRepo.updateKarma(authorUid, -1)
            }
            updateSubjectsList()
        }
    }

    fun updateUpVotes(subject: Subject, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't like your own post")
        val reviewId = subject.getId()

        viewModelScope.launch {
            // The user already liked the review
            if (subject.likers.contains(uid)) {
                // Remove a like
                subjectRepo.removeUpVote(reviewId, uid)
                userRepo.updateKarma(authorUid, -1)
            } else {
                // The user likes for the first time
                if (subject.dislikers.contains(uid)) {
                    // The user changed from dislike to like
                    subjectRepo.removeDownVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, 1)
                }
                // Add a like
                subjectRepo.addUpVote(subject.getId(), uid)
                userRepo.updateKarma(authorUid, 1)
            }
            updateSubjectsList()
        }
    }
}