package com.github.sdp.ratemyepfl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // We get a reference to the view objects, using their previously set ID
        val mNameText = findViewById<EditText>(R.id.mainName)
        val mGoButton = findViewById<Button>(R.id.mainGoButton)

        // We then set the behaviour of the button
        // It's quite short, so we can leave it here, but as soon as it starts
        // doing more complex stuff, it should be moved to a separate method
        mGoButton.setOnClickListener {
            val name = mNameText.text.toString()
            sayHello(name)
        }

        // Bonus: trigger the button when the user presses "enter" in the text field
        mNameText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mGoButton.callOnClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun sayHello(userName: String) {
        val intent = Intent(this, GreetingActivity::class.java)
        intent.putExtra(GreetingActivity.EXTRA_USER_NAME, userName)
        startActivity(intent)
    }
}
// coucou !