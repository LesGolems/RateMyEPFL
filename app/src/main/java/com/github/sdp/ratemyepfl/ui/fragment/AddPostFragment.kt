package com.github.sdp.ratemyepfl.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.post.Post
import com.github.sdp.ratemyepfl.utils.FragmentUtils.displayOnSnackbar
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

/**
 * Fragment for a post creation (Review, Subject, Comment)
 */
abstract class AddPostFragment<T : Post>(
    fragmentLayout: Int
) : Fragment(fragmentLayout) {

    companion object {
        fun onTextChangedTextWatcher(consume: (CharSequence?, Int, Int, Int) -> Unit): TextWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    consume(p0, p1, p2, p3)
                }

                override fun afterTextChanged(p0: Editable?) {}
            }
    }

    protected lateinit var indicationTitle: TextView
    protected lateinit var title: TextInputEditText
    protected lateinit var comment: TextInputEditText
    private lateinit var anonymousSwitch: SwitchCompat
    protected lateinit var doneButton: Button

    @Inject
    lateinit var auth: ConnectedUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        indicationTitle = view.findViewById(R.id.addPostIndicationTitle)
        title = view.findViewById(R.id.addPostTitle)
        comment = view.findViewById(R.id.addPostComment)
        anonymousSwitch = view.findViewById(R.id.anonymousSwitch)
        doneButton = view.findViewById(R.id.doneButton)

        setupListeners()
    }

    open fun setupListeners() {
        title.addTextChangedListener(onTextChangedTextWatcher { text, _, _, _ ->
            setTitle(text?.toString())
        })

        comment.addTextChangedListener(onTextChangedTextWatcher { text, _, _, _ ->
            setComment(text?.toString())
        })

        anonymousSwitch.setOnCheckedChangeListener { _, b ->
            setAnonymous(b)
        }

        doneButton.setOnClickListener {
            addPost()
        }
    }

    /**
     * Set the title of the post to [title]
     */
    abstract fun setTitle(title: String?)

    /**
     * Set the comment of the post to [comment]
     */
    abstract fun setComment(comment: String?)

    /**
     * Set the anonymity of the post to [anonymous]
     */
    abstract fun setAnonymous(anonymous: Boolean)

    /**
     * Add the post to the database
     */
    abstract fun addPost()

    /**
     * Once a post is submitted, all the information are reset to default
     */
    open fun reset() {
        title.setText("")
        comment.setText("")
    }

    /**
     * Checks if current user is logged in, otherwise display a warning
     */
    private fun checkLogin() {
        if (!auth.isLoggedIn())
            displayOnSnackbar(requireView(), "You need to login to be able to review")
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }
}