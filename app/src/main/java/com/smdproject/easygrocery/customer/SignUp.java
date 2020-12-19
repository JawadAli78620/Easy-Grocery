package com.smdproject.easygrocery.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smdproject.easygrocery.R;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private EditText mEmail, mPassword, mConfirmPsw;
    private TextView mSignInHere;
    private Button mRegBtn;

    private String email;
    private String psw, confPswrd;

    //Firebase Instances
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListner;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        /*firebaseAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    startActivity(new Intent(Register.this, CreateProfile.class));
                    finish();
                    return;
                }
            }
        };*/

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        mSignInHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }

    //Initiallize the views
    public void init(){
        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_psw);
        mConfirmPsw = findViewById(R.id.signup_confirmPsw);
        mSignInHere = findViewById(R.id.signup_signin);
        mRegBtn = findViewById(R.id.signup_SignupBtn);

        //get firebase instances
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //init Progress dialogue
        pd = new ProgressDialog(SignUp.this);
        pd.setTitle("Please Wait...");
    }

    public boolean validateFields(){
        email = mEmail.getText().toString();
        psw = mPassword.getText().toString();
        confPswrd = mConfirmPsw.getText().toString();

        if (email.isEmpty()) {
            mEmail.setError("Please Enter your Email");
            mEmail.requestFocus();
        } else if (psw.isEmpty()) {
            mPassword.setError("Please Enter a password");
            mPassword.requestFocus();
        } else if (confPswrd.isEmpty()) {
            mConfirmPsw.setError("Please Enter password to confirm");
            mConfirmPsw.requestFocus();
        } else if (!email.isEmpty() && !psw.isEmpty() && !confPswrd.isEmpty()) {
            if (psw.length() < 6) {
                mPassword.setError("password must be at least 6 characters long");
                mPassword.requestFocus();
            } else if (!psw.equals(confPswrd)) {
                mConfirmPsw.setError("please enter matching password");
                mConfirmPsw.requestFocus();
            } else {
                return true;
            }
        }
        return false;
    }

    public void createUser() {

        if (validateFields()) {

            pd.show();
            mAuth.createUserWithEmailAndPassword(email, psw)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                pd.dismiss();
                                Toast.makeText(SignUp.this, "Registered Successfully.", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                pd.dismiss();
                                Toast.makeText(SignUp.this, "Registeration Failed, Try Again..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}