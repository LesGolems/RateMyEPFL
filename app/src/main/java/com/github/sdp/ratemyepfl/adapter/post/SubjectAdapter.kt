package com.github.sdp.ratemyepfl.adapter.post

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.viewmodel.UserViewModel

class SubjectAdapter(
    override val lifecycleOwner: LifecycleOwner,
    override val userViewModel: UserViewModel,
    override val likeListener: OnClickListener<Subject>,
    override val dislikeListener: OnClickListener<Subject>,
    override val deleteListener: OnClickListener<Subject>,
    private val commentListener: OnClickListener<Subject>,
    override val profileClickListener: OnClickListener<Subject>
) : PostAdapter<Subject>(
    lifecycleOwner,
    userViewModel,
    likeListener,
    dislikeListener,
    deleteListener,
    profileClickListener,
    R.layout.subject_item
) {

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val postWithAuthor = getItem(position)
        val postView = holder.itemView

        val commentCount: TextView = postView.findViewById(R.id.subjectCommentCount)
        val commentButton: ImageButton = postView.findViewById(R.id.subjectCommentButton)
        val subjectKindIcon: ImageView = postView.findViewById(R.id.subjectKindIcon)
        val subjectKindText: TextView = postView.findViewById(R.id.subjectKindText)

        commentButton.setOnClickListener { commentListener.onClick(postWithAuthor) }
        commentCount.text = postWithAuthor.post.comments.size.toString()

        subjectKindIcon.setImageResource(postWithAuthor.post.kind.icon)
        subjectKindText.text = postWithAuthor.post.kind.id
    }
}