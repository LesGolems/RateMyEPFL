package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.ImageStorage
import com.github.sdp.ratemyepfl.database.UserRepository
import com.github.sdp.ratemyepfl.database.post.CommentRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.VoteException
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.review.CommentWithAuthor
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
    private val commentRepo: CommentRepository,
    private val userRepo: UserRepository,
    private val imageStorage: ImageStorage,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id: String = "ID"

    // Comments
    val comments = MutableLiveData<List<CommentWithAuthor>>()

    @Inject
    lateinit var auth: ConnectedUser

    fun updateCommentsList() {
        viewModelScope.launch {
            comments.postValue(commentRepo.getBySubjectId(id)
                .toMutableList()
                .map { comment ->
                    PostWithAuthor(
                        comment,
                        comment.uid?.let { userRepo.getUserByUid(it) },
                        comment.uid?.let { imageStorage.get(it) }
                    )
                }
                .sortedBy { cwa -> -cwa.post.likers.size })
        }
    }

    suspend fun removeComment(commentId: String) {
        commentRepo.remove(commentId).await()
        updateCommentsList()
    }

    fun updateDownVotes(comment: Comment, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't dislike your own review")
        val commentId = comment.getId()

        viewModelScope.launch {
            // The user already disliked the review
            if (comment.dislikers.contains(uid)) {
                // Remove a dislike
                commentRepo.removeDownVote(commentId, uid)
                userRepo.updateKarma(authorUid, 1)
            } else {
                // The user dislikes for the first time
                if (comment.likers.contains(uid)) {
                    // The user changed from like to dislike
                    commentRepo.removeUpVote(commentId, uid)
                    userRepo.updateKarma(authorUid, -1)
                }
                // Add a dislike
                commentRepo.addDownVote(comment.getId(), uid)
                userRepo.updateKarma(authorUid, -1)
            }
            updateCommentsList()
        }
    }

    fun updateUpVotes(comment: Comment, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't like your own review")
        val reviewId = comment.getId()

        viewModelScope.launch {
            // The user already liked the review
            if (comment.likers.contains(uid)) {
                // Remove a like
                commentRepo.removeUpVote(reviewId, uid)
                userRepo.updateKarma(authorUid, -1)
            } else {
                // The user likes for the first time
                if (comment.dislikers.contains(uid)) {
                    // The user changed from dislike to like
                    commentRepo.removeDownVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, 1)
                }
                // Add a like
                commentRepo.addUpVote(comment.getId(), uid)
                userRepo.updateKarma(authorUid, 1)
            }
            updateCommentsList()
        }
    }
}