package com.github.sdp.ratemyepfl

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.auth.UserAuth
import com.github.sdp.ratemyepfl.auth.UserAuthImpl

class MainActivity : AppCompatActivity() {
    private lateinit var mLoginButton: Button
    private lateinit var mEmail: TextView
    private lateinit var auth: UserAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // We get a reference to the view objects, using their previously set ID
        val mNameText = findViewById<EditText>(R.id.mainName)
        val mGoButton = findViewById<Button>(R.id.mainGoButton)
        mEmail = findViewById(R.id.email)
        mLoginButton = findViewById(R.id.loginButton)
        auth = UserAuthImpl()

        // We then set the behaviour of the button
        // It's quite short, so we can leave it here, but as soon as it starts
        // doing more complex stuff, it should be moved to a separate method
        mGoButton.setOnClickListener {
            val name = mNameText.text.toString()
            sayHello(name)
        }

        // Bonus: trigger the button when the user presses "enter" in the text field
        mNameText.setOnKeyListener{ _, keyCode, event ->
            if((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                mGoButton.performClick()
                true
            }
            false
        }

        setUpButtons()
    }

    private fun sayHello(userName: String) {
        val intent = Intent(this, GreetingActivity::class.java)
        intent.putExtra(GreetingActivity.EXTRA_USER_NAME, userName)
        startActivity(intent)
    }

    private fun setUpButtons(){
        mLoginButton.setOnClickListener {
            auth.signIn(this)
            mEmail.text = auth.getEmail()
            mEmail.visibility = View.VISIBLE
        }
    }


}