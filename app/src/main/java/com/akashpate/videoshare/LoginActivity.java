package com.akashpate.videoshare;


import android.content.Intent;
import android.nfc.TagLostException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity{

    EditText editEmail;
    EditText editPassword;
    Button loginBtn;
    Button registerBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        mAuth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.email_sign_in_button);
        registerBtn = findViewById(R.id.email_sign_up_button);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("User",currentUser.getEmail());
            startActivity(intent);
            finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                try {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("User", currentUser);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Registration Failed : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                try {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("User", currentUser);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed : "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}

