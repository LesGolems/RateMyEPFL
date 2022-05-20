package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.util.AdapterUtil
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.model.review.SubjectWithAuthor
import com.github.sdp.ratemyepfl.viewmodel.UserViewModel
import de.hdodenhof.circleimageview.CircleImageView

class SubjectAdapter(
    val lifecycleOwner: LifecycleOwner,
    val userViewModel: UserViewModel,
    val likeListener: OnClickListener<Subject>,
    val dislikeListener: OnClickListener<Subject>,
    val deleteListener: OnClickListener<Subject>,
    val profileClickListener: OnClickListener<Subject>,
) :
    ListAdapter<SubjectWithAuthor, SubjectAdapter.SubjectViewHolder>(AdapterUtil.diffCallback<SubjectWithAuthor>()) {

    inner class SubjectViewHolder(subjectView: View) :
        RecyclerView.ViewHolder(subjectView) {

        private val titleView: TextView = subjectView.findViewById(R.id.subjectTitle)
        private val commentView: TextView = subjectView.findViewById(R.id.subjectComment)
        private val dateView: TextView = subjectView.findViewById(R.id.subjectDate)

        private val likesTextView: TextView = subjectView.findViewById(R.id.subjectLikeCount)
        private val dislikesTextView: TextView = subjectView.findViewById(R.id.subjectDislikeCount)
        private val likeButton: ImageButton = subjectView.findViewById(R.id.subjectLikeButton)
        private val dislikeButton: ImageButton = subjectView.findViewById(R.id.subjectDislikeButton)

        private val deleteButton: ImageButton = subjectView.findViewById(R.id.subjectDeleteButton)

        private val authorUsername: TextView = subjectView.findViewById(R.id.authorUsername)
        private val authorProfilePicture: CircleImageView =
            subjectView.findViewById(R.id.authorProfilePicture)

        private lateinit var subject: Subject

        fun bind(subjectWithAuthor: SubjectWithAuthor) {
            subject = subjectWithAuthor.post

            titleView.text = subject.title
            commentView.text = subject.comment
            dateView.text = subject.date.toString()

            /* ==================== Like and dislike ==================== */
            likesTextView.text = subject.likers.size.toString()
            dislikesTextView.text = subject.dislikers.size.toString()

            dislikeButton.setOnClickListener {
                dislikeListener.onClick(subjectWithAuthor)
            }

            likeButton.setOnClickListener {
                likeListener.onClick(subjectWithAuthor)
            }

            deleteButton.setOnClickListener {
                deleteListener.onClick(subjectWithAuthor)
            }

            userViewModel.user.observe(lifecycleOwner) {
                /* Like button UI */
                likeButton.setImageResource(R.drawable.ic_like)
                dislikeButton.setImageResource(R.drawable.ic_dislike)
                if (it != null) {
                    // The user liked the review
                    if (subjectWithAuthor.post.likers.contains(it.uid)) {
                        likeButton.setImageResource(R.drawable.ic_like_toggled)
                    }
                    // The user disliked the review
                    if (subjectWithAuthor.post.dislikers.contains(it.uid)) {
                        dislikeButton.setImageResource(R.drawable.ic_dislike_toggled)
                    }
                }

                /* Delete button UI */
                if (it != null && it.isAdmin) {
                    deleteButton.visibility = View.VISIBLE
                } else {
                    deleteButton.visibility = View.INVISIBLE
                }
            }

            /* ==================== Author information ==================== */
            val author = subjectWithAuthor.author
            val image = subjectWithAuthor.image

            authorUsername.text = author?.username.orEmpty()
            authorProfilePicture.setOnClickListener {
                profileClickListener.onClick(subjectWithAuthor)
            }
            authorProfilePicture.setImageBitmap(image?.data)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectAdapter.SubjectViewHolder {
        return SubjectViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.subject_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubjectAdapter.SubjectViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }
}