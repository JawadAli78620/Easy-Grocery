package com.smdproject.easygrocery.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smdproject.easygrocery.R;

public class Login extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mSignInBtn;
    private TextView mSignupHere;

    FirebaseAuth auth;
    private ProgressDialog pd;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListner;

    String email, psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_psw);
        mSignInBtn = findViewById(R.id.login_SigninBtn);
        mSignupHere = findViewById(R.id.login_signup);

        auth = FirebaseAuth.getInstance();

        authStateListener();

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignInPressed();
            }
        });

        mSignupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        //init Progress dialogue
        pd = new ProgressDialog(Login.this);
        pd.setTitle("Please Wait...");
    }

    public boolean validateFields(){
        email = mEmail.getText().toString();
        psw = mPassword.getText().toString();

        if (email.isEmpty()) {
            mEmail.setError("Please Enter your Email");
            mEmail.requestFocus();
        } else if (psw.isEmpty()) {
            mPassword.setError("Please Enter a password");
            mPassword.requestFocus();
        } else if (!email.isEmpty() && !psw.isEmpty()) {
            if (psw.length() < 6) {
                mPassword.setError("password must be at least 6 characters long");
                mPassword.requestFocus();
            }else {
                return true;
            }
        }
        return false;
    }

    public void onSignInPressed(){
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if(validateFields()) {
            pd.show();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                Toast.makeText(Login.this, "Successfully Log In..", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            } else {
                                pd.dismiss();
                                Toast.makeText(Login.this, "Sign in failed, Please Sign up first.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void authStateListener(){
        firebaseAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                    return;
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthStateListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(firebaseAuthStateListner);
    }
}