package com.github.sdp.ratemyepfl.adapter.post

import android.widget.ImageButton
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

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val postWithAuthor = getItem(position)
        val postView = holder.itemView

        val commentButton: ImageButton = postView.findViewById(R.id.subjectCommentButton)
        val subjectKind: TextView = postView.findViewById(R.id.subjectKind)
        val commentCount: TextView = postView.findViewById(R.id.subjectCommentCount)

        commentButton.setOnClickListener {
            commentListener.onClick(postWithAuthor)
        }

        subjectKind.text = "FOOD"

        commentCount.text = postWithAuthor.post.commentators.size.toString()
    }
}