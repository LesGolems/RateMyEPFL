package com.github.sdp.ratemyepfl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class GreetingActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_NAME: String = "username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        val name = intent.getStringExtra(EXTRA_USER_NAME) // Get the `extra` containing the name of the user

        var textView = findViewById<TextView>(R.id.greetingMessage)
        textView.text = getString(R.string.greeting_message, name) // Set the text of the field
    }
}