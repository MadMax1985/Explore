package android.ufrpe.com.explore

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {

    private var signUp: Button? = null
    private var login: Button? = null

    private var editTextEmailsignin: EditText? = null
    private var editTextPasswordsignin: EditText? = null
    private var progressDialog: ProgressDialog? = null
    internal var progressBar: ProgressBar? = null
    internal var dataBaseUsers: DatabaseReference? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dataBaseUsers = FirebaseDatabase.getInstance().getReference("users")
        mAuth = FirebaseAuth.getInstance()

        signUp = findViewById(R.id.signUp) as Button
        login = findViewById(R.id.login) as Button



        editTextEmailsignin = findViewById(R.id.editTextEmail) as EditText
        editTextPasswordsignin = findViewById(R.id.editTextPassword) as EditText

        progressBar = findViewById(R.id.progressBar) as ProgressBar

        progressDialog = ProgressDialog(this)




        login!!.setOnClickListener { log_user() }

        signUp!!.setOnClickListener { openSignUp() }

    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!= null){
            finish();
            startActivity(new Intent(this,Profile.class));
        }
    }
    */

    fun log_user() {
        Log.v("Erro", "TESTE")

        val email = editTextEmailsignin!!.text.toString().trim { it <= ' ' }
        val password = editTextPasswordsignin!!.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            editTextEmailsignin!!.error = "Email is required"
            editTextEmailsignin!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailsignin!!.error = "Enter a valid email"
            editTextEmailsignin!!.requestFocus()
            return
        }
        if (password.isEmpty()) {
            editTextPasswordsignin!!.error = "Password is required"
            editTextPasswordsignin!!.requestFocus()
            return

        }

        if (password.length < 6) {
            editTextPasswordsignin!!.error = "Minimum length of password should be 6"
            editTextPasswordsignin!!.requestFocus()
            return
        }

        //progressBar.setVisibility(View.VISIBLE);
        progressDialog!!.setMessage("Please Wait...")
        progressDialog!!.show()



        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //progressBar.setVisibility(View.GONE);
                finish()
                Toast.makeText(applicationContext, "User found Succesfully", Toast.LENGTH_SHORT).show()
                var imgUrl: String? = null
                if (mAuth!!.getCurrentUser()!!.getPhotoUrl() != null) {
                    imgUrl = mAuth!!.getCurrentUser()!!.getPhotoUrl()!!.toString()
                }

                val user = User(mAuth!!.getCurrentUser()!!.getEmail()!!.toString(), imgUrl!!)
                dataBaseUsers!!.child(mAuth!!.getCurrentUser()!!.getUid()).setValue(user)

                openProfile()
            } else {
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(applicationContext, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
            progressDialog!!.dismiss()
        }
    }

    fun openSignUp() {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }

    fun openProfile() {
        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }

}