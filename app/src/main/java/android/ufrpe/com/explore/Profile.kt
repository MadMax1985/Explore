package android.ufrpe.com.explore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast

import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

import java.io.IOException

class Profile : AppCompatActivity() {
    internal var imageView: ImageView? = null
    internal var editText: EditText? = null
    internal var progressBarUse: ProgressBar? = null
    internal var buttonUserProfile: Button? = null
    internal var buttonNext: Button? = null
    internal var profileImageUrl: String? = null


    internal var mAuth: FirebaseAuth? = null
    internal var uriProfileImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance()


        imageView = findViewById(R.id.imageUserView) as ImageView
        editText = findViewById(R.id.editUserName) as EditText
        progressBarUse = findViewById(R.id.progressBarProfile) as ProgressBar
        buttonUserProfile = findViewById(R.id.saveUserInfo) as Button
        buttonNext = findViewById(R.id.userList) as Button

        imageView!!.setOnClickListener { showImageChooser() }

        buttonUserProfile!!.setOnClickListener { saveUserInformation() }

        buttonNext!!.setOnClickListener { openUserList() }

        loadUserInformation()
    }

    fun openUserList() {
        val intent = Intent(this, UserList::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuLogout -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                startActivity(Intent(this, Login::class.java))
            }
        }
        return true
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()== null){
            finish();
            startActivity(new Intent(this,Login.class));
        }
    }
*/

    //Load picture and name if information was previously saved to database
    private fun loadUserInformation() {

        val user = mAuth!!.currentUser
        if (user!!.photoUrl != null) {
            Glide.with(this)
                    .load(user.photoUrl!!.toString())
                    .into(imageView)
        }
        if (user.displayName != null) {
            editText!!.setText(user.displayName)
        }
    }

    //Save user information to Firebase
    private fun saveUserInformation() {
        val displayName = editText!!.text.toString()
        if (displayName.isEmpty()) {
            editText!!.error = "Name Required"
            editText!!.requestFocus()
            return
        }

        val user = mAuth!!.currentUser

        if (user != null && profileImageUrl != null) {
            val profile = UserProfileChangeRequest.Builder().setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build()

            user.updateProfile(profile)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@Profile, "Profile Updated", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    //Render picture to upload on firebase storage
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            uriProfileImage = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriProfileImage)
                imageView!!.setImageBitmap(bitmap)
                uploadImageToFirebaseStorage()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    //Upload image to firebase

    private fun uploadImageToFirebaseStorage() {
        val profileImageReference = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg")

        if (uriProfileImage != null) {
            progressBarUse!!.visibility = View.VISIBLE
            profileImageReference.putFile(uriProfileImage!!).addOnSuccessListener { taskSnapshot ->
                progressBarUse!!.visibility = View.GONE
                profileImageUrl = taskSnapshot.downloadUrl!!.toString()
            }.addOnFailureListener { e ->
                progressBarUse!!.visibility = View.GONE
                Toast.makeText(this@Profile, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    //Shows icon to choose picture from gallery
    private fun showImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select profile image"), CHOOSE_IMAGE)
    }

    companion object {
        private val CHOOSE_IMAGE = 101
    }
}
