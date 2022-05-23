package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.post.PostRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.VoteException
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Post
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

abstract class PostListViewModel<T : Post>(
    open val postRepo: PostRepository<T>,
    open val userRepo: UserRepository,
    open val imageStorage: Storage<ImageFile>
) : ViewModel() {

    // Posts
    val posts = MutableLiveData<List<PostWithAuthor<T>>>()

    @Inject
    lateinit var auth: ConnectedUser

    init {
        updatePostsList()
    }

    abstract suspend fun fetchAllPosts(): List<T>

    fun updatePostsList() {
        viewModelScope.launch {
            posts.postValue(fetchAllPosts()
                .toMutableList()
                .map { post ->
                    PostWithAuthor(
                        post,
                        post.uid?.let { userRepo.getUserByUid(it) },
                        post.uid?.let { imageStorage.get(it) }
                    )
                }
                .sortedBy { postWithAuthor -> -postWithAuthor.post.likers.size })
        }
    }

    open fun updateUpVotes(post: T, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't like your own post")
        val reviewId = post.getId()

        viewModelScope.launch {
            // The user already liked the post
            if (post.likers.contains(uid)) {
                // Remove a like
                postRepo.removeUpVote(reviewId, uid)
                userRepo.updateKarma(authorUid, -1)
            } else {
                // The user likes for the first time
                if (post.dislikers.contains(uid)) {
                    // The user changed from dislike to like
                    postRepo.removeDownVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, 1)
                }
                // Add a like
                postRepo.addUpVote(post.getId(), uid)
                userRepo.updateKarma(authorUid, 1)
            }
            updatePostsList()
        }
    }

    open fun updateDownVotes(post: T, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't dislike your own review")
        val commentId = post.getId()

        viewModelScope.launch {
            // The user already disliked the review
            if (post.dislikers.contains(uid)) {
                // Remove a dislike
                postRepo.removeDownVote(commentId, uid)
                userRepo.updateKarma(authorUid, 1)
            } else {
                // The user dislikes for the first time
                if (post.likers.contains(uid)) {
                    // The user changed from like to dislike
                    postRepo.removeUpVote(commentId, uid)
                    userRepo.updateKarma(authorUid, -1)
                }
                // Add a dislike
                postRepo.addDownVote(post.getId(), uid)
                userRepo.updateKarma(authorUid, -1)
            }
            updatePostsList()
        }
    }

    open fun removePost(postId: String) {
        viewModelScope.launch {
            postRepo.remove(postId).await()
            updatePostsList()
        }
    }
}