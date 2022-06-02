package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.backend.database.post.SubjectRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.VoteException
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.model.review.SubjectWithAuthor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val subjectRepo: SubjectRepository,
    private val userRepo: UserRepository,
    private val imageStorage: Storage<ImageFile>
) : ViewModel() {

    // Subjects
    val subjects = MutableLiveData<List<SubjectWithAuthor>>()

    val isEmpty: LiveData<Boolean> = subjects.map { it.isEmpty() }

    @Inject
    lateinit var auth: ConnectedUser

    fun getSubjects(): Flow<List<SubjectWithAuthor>> =
        subjectRepo.get()
            .map { subjects ->
                subjects.map { subject ->
                    SubjectWithAuthor(
                        subject,
                        subject.uid?.let { userRepo.getUserByUid(it) },
                        subject.uid?.let { imageStorage.get(it).lastOrNull() }
                    )
                }.sortedBy { rwa -> -rwa.obj.likers.size }
            }

    fun removeSubject(subjectId: String) {
        viewModelScope.launch {
            subjectRepo.remove(subjectId).collect()
        }
    }

    private fun addLikeInLiveData(id: String, uid: String?, posts: List<SubjectWithAuthor>?) =
        posts?.map {
            val subject = it.obj
            if (subject.getId() == id && uid != null) {
                SubjectWithAuthor(
                    subject.copy(likers = subject.likers.plus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }

    private fun removeLikeInLiveData(id: String, uid: String?, posts: List<SubjectWithAuthor>?) =
        posts?.map {
            val subject = it.obj
            if (subject.getId() == id && uid != null) {
                SubjectWithAuthor(
                    subject.copy(likers = subject.likers.minus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }

    private fun addDislikeInLiveData(id: String, uid: String?, posts: List<SubjectWithAuthor>?) =
        posts?.map {
            val subject = it.obj
            if (subject.getId() == id && uid != null) {
                SubjectWithAuthor(
                    subject.copy(dislikers = subject.dislikers.plus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }

    private fun removeDislikeInLiveData(id: String, uid: String?, posts: List<SubjectWithAuthor>?) =
        posts?.map {
            val subject = it.obj
            if (subject.getId() == id && uid != null) {
                SubjectWithAuthor(
                    subject.copy(dislikers = subject.dislikers.minus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }

    fun updateDownVotes(subject: Subject, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't dislike your own post")
        val subjectId = subject.getId()
        var posts = subjects.value

        viewModelScope.launch {
            // The user already disliked the review
            if (subject.dislikers.contains(uid)) {
                // Remove a dislike
                subjectRepo.removeDownVote(subjectId, uid)
                userRepo.updateKarma(authorUid, 1)
                posts = removeDislikeInLiveData(subject.getId(), uid, posts)
            } else {
                // The user dislikes for the first time
                if (subject.likers.contains(uid)) {
                    // The user changed from like to dislike
                    subjectRepo.removeUpVote(subjectId, uid)
                    userRepo.updateKarma(authorUid, -1)
                    posts = removeLikeInLiveData(subject.getId(), uid, posts)
                }
                // Add a dislike
                subjectRepo.addDownVote(subject.getId(), uid)
                userRepo.updateKarma(authorUid, -1)
                posts = addDislikeInLiveData(subject.getId(), uid, posts)
            }
            posts.let { subjects.postValue(it) }
        }
    }

    fun updateUpVotes(subject: Subject, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't like your own post")
        val reviewId = subject.getId()
        var posts = subjects.value

        viewModelScope.launch {
            // The user already liked the review
            if (subject.likers.contains(uid)) {
                // Remove a like
                subjectRepo.removeUpVote(reviewId, uid)
                userRepo.updateKarma(authorUid, -1)
                posts = removeLikeInLiveData(subject.getId(), uid, posts)
            } else {
                // The user likes for the first time
                if (subject.dislikers.contains(uid)) {
                    // The user changed from dislike to like
                    subjectRepo.removeDownVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, 1)
                    posts = removeDislikeInLiveData(subject.getId(), uid, posts)
                }
                // Add a like
                subjectRepo.addUpVote(subject.getId(), uid)
                userRepo.updateKarma(authorUid, 1)
                posts = addLikeInLiveData(subject.getId(), uid, posts)
            }
            posts.let { subjects.postValue(it) }
        }
    }
}