package com.example.mybook;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mybook.Models.Users;
import com.example.mybook.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Welcome.......");




        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


// Sign Up Function
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.editTextUserName.getText().toString().isEmpty()){
                    binding.editTextUserName.setError("Enter User Name!");
                    return;
                }
                if(binding.editTextUserEmailId.getText().toString().isEmpty()){
                    binding.editTextUserEmailId.setError("Enter Your Email Id!");
                    return;
                }
                if(binding.editTextUserPassword.getText().toString().isEmpty()){
                    binding.editTextUserPassword.setError("Please Create Password!");
                    return;
                }
                progressDialog.show();


                auth.createUserWithEmailAndPassword(binding.editTextUserEmailId.getText().toString(),binding.editTextUserPassword.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
//                                    Using User Model to store the data of user in the database.
                                    Users User = new Users(binding.editTextUserName.getText().toString(),binding.editTextUserEmailId.getText().toString()
                                            ,binding.editTextUserPassword.getText().toString());
//                                    geting the the userId from database
                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(User);

                                    Toast.makeText(SignUpActivity.this, "Welcome | Sign Up Successfull !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    FirebaseUser user = auth.getCurrentUser();
                                }else {
                                    Toast.makeText(SignUpActivity.this, "Sign Up Failed !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding.btnGSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        binding.HaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}