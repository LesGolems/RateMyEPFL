package com.github.sdp.ratemyepfl.adapter

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
    override val profileClickListener: OnClickListener<Subject>,
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

        holder.itemView.findViewById<ImageButton>(R.id.subjectCommentButton).setOnClickListener {

        }

        holder.itemView.findViewById<TextView>(R.id.subjectKind).text =
            "FOOD"

        holder.itemView.findViewById<TextView>(R.id.subjectCommentCount).text =
            postWithAuthor.post.commentators.size.toString()
    }
}