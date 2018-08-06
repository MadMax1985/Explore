package android.ufrpe.com.explore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private Button signUp, login;

    private EditText editTextEmailsignin, editTextPasswordsignin;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    DatabaseReference dataBaseUsers;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dataBaseUsers = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        signUp = (Button) findViewById(R.id.signUp);
        login = (Button) findViewById(R.id.login);



        editTextEmailsignin = (EditText) findViewById(R.id.editTextEmail);
        editTextPasswordsignin = (EditText) findViewById(R.id.editTextPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressDialog = new ProgressDialog(this);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_user();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();

            }
        });

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

    public void log_user(){
        Log.v("Erro","TESTE");

        String email = editTextEmailsignin.getText().toString().trim();
        String password = editTextPasswordsignin.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmailsignin.setError("Email is required");
            editTextEmailsignin.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailsignin.setError("Enter a valid email");
            editTextEmailsignin.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPasswordsignin.setError("Password is required");
            editTextPasswordsignin.requestFocus();
            return;

        }

        if(password.length()<6){
            editTextPasswordsignin.setError("Minimum length of password should be 6");
            editTextPasswordsignin.requestFocus();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();



        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //progressBar.setVisibility(View.GONE);
                    finish();
                    Toast.makeText(getApplicationContext(), "User found Succesfully", Toast.LENGTH_SHORT).show();
                    String imgUrl = null;
                    if(mAuth.getInstance().getCurrentUser().getPhotoUrl()!= null){
                        imgUrl = mAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
                    }

                    User user = new User(mAuth.getInstance().getCurrentUser().getEmail().toString(),imgUrl);
                    dataBaseUsers.child(mAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                    openProfile();
                }else{
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Some error occurred", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

        });
    }

    public void openSignUp(){
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }

    public void openProfile(){
        Intent intent = new Intent(this,Profile.class);
        startActivity(intent);
    }

}