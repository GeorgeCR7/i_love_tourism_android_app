package com.example.login_register_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, passwordConfirm;

    Button btnRegister, btnGoLogin, btnLanguage;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.emailRegister);
        password = findViewById(R.id.passwordRegister);
        passwordConfirm = findViewById(R.id.passwordRegConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoLogin = findViewById(R.id.btnGoLogin);
        btnLanguage = findViewById(R.id.btnLanguage);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LanguageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(){

        String strEmail = email.getText().toString();
        String strPassword = password.getText().toString();
        String strPasswordConfirm = passwordConfirm.getText().toString();

        if (strEmail.isEmpty()) {
            email.setError(getResources().getString(R.string.email_empty));
            email.requestFocus();
            return;
        } else if (!isEmailValid(strEmail)) {
            email.setError(getResources().getString(R.string.email_not_valid));
            email.requestFocus();
            return;
        } else if (strPassword.isEmpty()) {
            password.setError(getResources().getString(R.string.password_empty));
            password.requestFocus();
            return;
        } else if (strPassword.length() < 6) {
            password.setError(getResources().getString(R.string.password_length_error));
            password.requestFocus();
            return;
        } else if (strPasswordConfirm.isEmpty()) {
            passwordConfirm.setError(getResources().getString(R.string.password_confirm));
            passwordConfirm.requestFocus();
            return;
        } else if (!strPassword.equals(strPasswordConfirm)) {
            Toast.makeText(RegisterActivity.this,
                    R.string.passwords_not_match,
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    R.string.success_register,
                                    Toast.LENGTH_SHORT).show();
                            //sendEmailVerification(strEmail);
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    R.string.error_register + "" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }
    }

    private static boolean isEmailValid (String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    // TODO: Find how to send an email verification after new user register.
    private void sendEmailVerification (String email) {

    }
}