package com.example.login_register_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_register_app.R;
import com.example.login_register_app.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, passwordConfirm;

    Button btnRegister, btnGoLogin, btnLanguage;

    // Firebase object for authentication.
    FirebaseAuth mAuth;

    // Firebase objects for reading database.
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

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

        rootNode = FirebaseDatabase.getInstance();
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
                intent.putExtra("LANG_ACTV","register");
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(){

        String strEmail = email.getText().toString();
        String strPassword = password.getText().toString();
        String strPasswordConfirm = passwordConfirm.getText().toString();

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");

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
                            // Send verification email to the new user.
                            sendEmailVerification(strEmail);
                            // Create a new User object, at first, only with email.
                            User user = new User(strEmail, "", "", setDateCreated(), "");
                            // Store the new User to the Firebase.
                            reference.child(strEmail.replace(".","")).setValue(user);

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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

    private String setDateCreated() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        String day, month;

        if(cal.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + cal.get(Calendar.DAY_OF_MONTH)+".";
        } else {
            day = ""+cal.get(Calendar.DAY_OF_MONTH)+".";
        }

        if((cal.get(Calendar.MONTH)+1) < 10) {
            month = ".0" + (cal.get(Calendar.MONTH)+1)+".";
        } else {
            month = ""+(cal.get(Calendar.MONTH)+1)+".";
        }

        return day + month + cal.get(Calendar.YEAR);
    }

    // TODO: This function is not working, find another solution.
    private void sendEmailVerification (String receiverEmail) {

        try {
            String senderEmail = "xcr471x@gmail.com";
            String senderEmailPass = "Test*123";
            String host = "smtp.gmail.com";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderEmailPass);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            mimeMessage.setSubject("I LOVE TOURISM Application!");
            mimeMessage.setText("Hi! This is just an email from my app!");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}