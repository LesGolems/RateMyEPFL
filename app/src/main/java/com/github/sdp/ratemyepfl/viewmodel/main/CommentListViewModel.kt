package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.post.CommentRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.post.SubjectRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.exceptions.VoteException
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.review.CommentWithAuthor
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import com.github.sdp.ratemyepfl.model.time.DateTime
import com.github.sdp.ratemyepfl.viewmodel.AddPostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
    private val subjectRepo: SubjectRepository,
    private val commentRepo: CommentRepository,
    private val connectedUser: ConnectedUser,
    private val userRepo: UserRepository,
    private val imageStorage: Storage<ImageFile>,
) : AddPostViewModel<Comment>() {

    lateinit var id: String

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
                        comment.uid?.let { imageStorage.get(it).catch {  }.lastOrNull() }
                    )
                }
                .sortedBy { cwa -> -cwa.post.likers.size })
        }
    }

    fun removeComment(commentId: String) {
        viewModelScope.launch {
            commentRepo.remove(commentId).await()
            subjectRepo.removeComment(id, commentId)
            updateCommentsList()
        }
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

    fun submitComment() {
        val comment = comment.value
        val date = DateTime.now()
        var uid: String? = null

        // only connected users may add reviews
        if (!connectedUser.isLoggedIn()) {
            throw DisconnectedUserException()
        } else if (comment.isNullOrEmpty()) {
            throw MissingInputException(EMPTY_COMMENT_MESSAGE)
        }

        if (!anonymous.value!!) {
            uid = connectedUser.getUserId()
        }

        val builder = Comment.Builder()
            .setSubjectID(id)
            .setComment(comment)
            .setDate(date)
            .setUid(uid)

        try {
            val com = builder.build()
            viewModelScope.launch(Dispatchers.IO) {
                val commentId = commentRepo.add(com).await()
                subjectRepo.addComment(id, commentId)
                updateCommentsList()
            }
        } catch (e: IllegalStateException) {
            throw IllegalStateException("Failed to build the comment (from ${e.message}")
        }
    }
}