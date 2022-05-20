package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.util.AdapterUtil
import com.github.sdp.ratemyepfl.model.review.Post
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import com.github.sdp.ratemyepfl.viewmodel.UserViewModel
import de.hdodenhof.circleimageview.CircleImageView

fun interface OnClickListener<U : Post> {
    fun onClick(post: PostWithAuthor<U>)
}

open class PostAdapter<T : Post>(
    open val lifecycleOwner: LifecycleOwner,
    open val userViewModel: UserViewModel,
    open val likeListener: OnClickListener<T>,
    open val dislikeListener: OnClickListener<T>,
    open val deleteListener: OnClickListener<T>,
    open val profileClickListener: OnClickListener<T>,
    private val layout: Int
) : ListAdapter<
        PostWithAuthor<T>,
        PostAdapter<T>.PostViewHolder>(AdapterUtil.diffCallback<PostWithAuthor<T>>()) {


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    open inner class PostViewHolder(postView: View) :
        RecyclerView.ViewHolder(postView) {

        private val titleView: TextView = postView.findViewById(R.id.titleReview)
        private val commentView: TextView = postView.findViewById(R.id.commentReview)
        private val dateView: TextView = postView.findViewById(R.id.dateReview)

        private val likesTextView: TextView = postView.findViewById(R.id.likeCount)
        private val dislikesTextView: TextView = postView.findViewById(R.id.dislikeCount)
        private val likeButton: ImageButton = postView.findViewById(R.id.likeButton)
        private val dislikeButton: ImageButton = postView.findViewById(R.id.dislikeButton)

        private val deleteButton: ImageButton = postView.findViewById(R.id.deleteButton)

        private val authorUsername: TextView = postView.findViewById(R.id.author_username)
        private val authorProfilePicture: CircleImageView =
            postView.findViewById(R.id.author_profile_picture)

        protected lateinit var post: T

        open fun bind(postWithAuthor: PostWithAuthor<T>) {
            post = postWithAuthor.post

            titleView.text = post.title
            commentView.text = post.comment
            dateView.text = post.date.toString()

            /* ==================== Like and dislike ==================== */
            likesTextView.text = post.likers.size.toString()
            dislikesTextView.text = post.dislikers.size.toString()

            dislikeButton.setOnClickListener {
                dislikeListener.onClick(postWithAuthor)
            }

            likeButton.setOnClickListener {
                likeListener.onClick(postWithAuthor)
            }

            deleteButton.setOnClickListener {
                deleteListener.onClick(postWithAuthor)
            }

            userViewModel.user.observe(lifecycleOwner) {
                /* Like button UI */
                likeButton.setImageResource(R.drawable.ic_like)
                dislikeButton.setImageResource(R.drawable.ic_dislike)
                if (it != null) {
                    // The user liked the review
                    if (postWithAuthor.post.likers.contains(it.uid)) {
                        likeButton.setImageResource(R.drawable.ic_like_toggled)
                    }
                    // The user disliked the review
                    if (postWithAuthor.post.dislikers.contains(it.uid)) {
                        dislikeButton.setImageResource(R.drawable.ic_dislike_toggled)
                    }
                }

                /* Delete button UI */
                if (it != null && it.isAdmin) {
                    deleteButton.visibility = VISIBLE
                } else {
                    deleteButton.visibility = INVISIBLE
                }
            }

            /* ==================== Author information ==================== */
            val author = postWithAuthor.author
            val image = postWithAuthor.image

            authorUsername.text = author?.username.orEmpty()
            authorProfilePicture.setOnClickListener {
                profileClickListener.onClick(postWithAuthor)
            }
            authorProfilePicture.setImageBitmap(image?.data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postWithAuthor = getItem(position)
        holder.bind(postWithAuthor)
    }

}