package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.backend.database.post.CommentRepository
import com.github.sdp.ratemyepfl.backend.database.post.SubjectRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.exceptions.VoteException
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.review.CommentWithAuthor
import com.github.sdp.ratemyepfl.model.time.DateTime
import com.github.sdp.ratemyepfl.viewmodel.AddPostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    val isEmpty: LiveData<Boolean> = comments.map { it.isEmpty() }

    @Inject
    lateinit var auth: ConnectedUser

    fun getComments() = commentRepo.getBySubjectId(id)
        .map { comments ->
            comments.map { comment ->
                CommentWithAuthor(
                    comment,
                    comment.uid?.let { userRepo.getUserByUid(it) },
                    comment.uid?.let { imageStorage.get(it).catch { }.lastOrNull() }
                )
            }
                .sortedBy { cwa -> -cwa.obj.likers.size }
        }

    fun removeComment(commentId: String) {
        viewModelScope.launch {
            commentRepo.remove(commentId).collect()
            subjectRepo.removeComment(id, commentId)
        }
    }

    private fun addLikeInLiveData(id: String, uid: String?, posts: List<CommentWithAuthor>?) =
        posts?.map {
            val comment = it.obj
            if (comment.getId() == id && uid != null) {
                CommentWithAuthor(
                    comment.copy(likers = comment.likers.plus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }

    private fun removeLikeInLiveData(id: String, uid: String?, posts: List<CommentWithAuthor>?) =
        posts?.map {
            val comment = it.obj
            if (comment.getId() == id && uid != null) {
                CommentWithAuthor(
                    comment.copy(likers = comment.likers.minus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }

    private fun addDislikeInLiveData(id: String, uid: String?, posts: List<CommentWithAuthor>?) =
        posts?.map {
            val comment = it.obj
            if (comment.getId() == id && uid != null) {
                CommentWithAuthor(
                    comment.copy(dislikers = comment.dislikers.plus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }

    private fun removeDislikeInLiveData(id: String, uid: String?, posts: List<CommentWithAuthor>?) =
        posts?.map {
            val comment = it.obj
            if (comment.getId() == id && uid != null) {
                CommentWithAuthor(
                    comment.copy(dislikers = comment.dislikers.minus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }

    fun updateDownVotes(comment: Comment, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't dislike your own review")
        val commentId = comment.getId()
        var posts = comments.value

        viewModelScope.launch {
            // The user already disliked the review
            if (comment.dislikers.contains(uid)) {
                // Remove a dislike
                commentRepo.removeDownVote(commentId, uid)
                userRepo.updateKarma(authorUid, 1)
                posts = removeDislikeInLiveData(comment.getId(), uid, posts)
            } else {
                // The user dislikes for the first time
                if (comment.likers.contains(uid)) {
                    // The user changed from like to dislike
                    commentRepo.removeUpVote(commentId, uid)
                    userRepo.updateKarma(authorUid, -1)
                    posts = removeLikeInLiveData(comment.getId(), uid, posts)
                }
                // Add a dislike
                commentRepo.addDownVote(comment.getId(), uid)
                userRepo.updateKarma(authorUid, -1)
                posts = addDislikeInLiveData(comment.getId(), uid, posts)
            }
            posts.let { comments.postValue(it) }
        }
    }

    fun updateUpVotes(comment: Comment, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't like your own review")
        val reviewId = comment.getId()
        var posts = comments.value

        viewModelScope.launch {
            // The user already liked the review
            if (comment.likers.contains(uid)) {
                // Remove a like
                commentRepo.removeUpVote(reviewId, uid)
                userRepo.updateKarma(authorUid, -1)
                posts = removeLikeInLiveData(comment.getId(), uid, posts)
            } else {
                // The user likes for the first time
                if (comment.dislikers.contains(uid)) {
                    // The user changed from dislike to like
                    commentRepo.removeDownVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, 1)
                    posts = removeDislikeInLiveData(comment.getId(), uid, posts)
                }
                // Add a like
                commentRepo.addUpVote(comment.getId(), uid)
                userRepo.updateKarma(authorUid, 1)
                posts = addLikeInLiveData(comment.getId(), uid, posts)
            }
            posts.let { comments.postValue(it) }
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

        val com = Comment(subjectId = id, comment = comment, date = date, uid = uid)
        viewModelScope.launch(Dispatchers.IO) {
            val commentId = commentRepo.add(com).last()
            subjectRepo.addComment(id, commentId)
        }
    }
}