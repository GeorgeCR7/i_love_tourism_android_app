package com.example.login_register_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnImageLabeling, btnTxtRecognition, btnSights;
    Button btnLogout, btnAbout;

    TextView txtAppTitle, txtDate;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnImageLabeling = findViewById(R.id.btnImageLabeling);
        btnTxtRecognition = findViewById(R.id.btnTxtRecognition);
        btnSights = findViewById(R.id.btnSights);
        btnLogout = findViewById(R.id.btnLogout);
        btnAbout = findViewById(R.id.btnAbout);

        txtAppTitle = findViewById(R.id.txtAppTitle);
        txtDate = findViewById(R.id.txtDate);

        mAuth = FirebaseAuth.getInstance();

        colorMainAppTitle(txtAppTitle.getText().toString());
        setCurrentDate();

        btnImageLabeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImgLabelingActivity.class);
                startActivity(intent);
            }
        });

        btnSights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SightsActivity.class);
                startActivity(intent);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutWindow();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this,
                        R.string.log_out_main,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showAboutWindow () {
        AboutWindow aboutWindow = new AboutWindow();
        aboutWindow.show(getSupportFragmentManager(), "About Window");
    }

    private void colorMainAppTitle (String strTxtAppTitle) {
        SpannableString ss = new SpannableString(strTxtAppTitle);
        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
        ss.setSpan(fcsRed, 2,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtAppTitle.setText(ss);
    }

    private void setCurrentDate () {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        if ((cal.get(Calendar.MONTH)+1) < 10) {
            txtDate.setText(cal.get(Calendar.DAY_OF_MONTH) + ".0"
                    + (cal.get(Calendar.MONTH)+1) + "." +
                    cal.get(Calendar.YEAR));
        } else {
            txtDate.setText(cal.get(Calendar.DAY_OF_MONTH) + "."
                    + (cal.get(Calendar.MONTH)+1) + "." +
                    cal.get(Calendar.YEAR));
        }
    }
}