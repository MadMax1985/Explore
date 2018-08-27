package android.ufrpe.com.explore

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    internal var editTextEmail: EditText? = null
    internal var editTextPassword: EditText? = null
    private var register: Button? = null
    internal var progressbar: ProgressBar? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editTextEmail = findViewById(R.id.editTextEmail) as EditText
        editTextPassword = findViewById(R.id.editTextPassword) as EditText

        progressbar = findViewById(R.id.progressBar) as ProgressBar

        mAuth = FirebaseAuth.getInstance()

        register = findViewById(R.id.button_register) as Button
        register!!.setOnClickListener { registerUser() }

    }

    private fun registerUser() {
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            editTextEmail!!.error = "Email is required"
            editTextEmail!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail!!.error = "Enter a valid email"
            editTextEmail!!.requestFocus()
            return
        }
        if (password.isEmpty()) {
            editTextPassword!!.error = "Password is required"
            editTextPassword!!.requestFocus()
            return

        }

        if (password.length < 6) {
            editTextPassword!!.error = "Minimum length of password should be 6"
            editTextPassword!!.requestFocus()
            return
        }

        progressbar!!.visibility = View.VISIBLE
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressbar!!.visibility = View.GONE
                Toast.makeText(applicationContext, "User registered Succesfully", Toast.LENGTH_SHORT).show()

            } else {
                progressbar!!.visibility = View.GONE
                Toast.makeText(applicationContext, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
