package com.example.login_register_app.activities;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_register_app.AboutWindow;
import com.example.login_register_app.R;
import com.google.firebase.auth.FirebaseAuth;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnImageLabeling, btnTxtRecognition, btnSights;
    Button btnEditProfile, btnAbout, btnLogout;

    TextView txtAppTitle, txtDate;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnImageLabeling = findViewById(R.id.btnImageLabeling);
        btnTxtRecognition = findViewById(R.id.btnTxtRecognition);
        btnSights = findViewById(R.id.btnSights);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnAbout = findViewById(R.id.btnAbout);
        btnLogout = findViewById(R.id.btnLogout);

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
                finish();
            }
        });

        btnTxtRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TxtRecognitionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SightsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
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

    private void setCurrentDate() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        String dayName = null, day, month;

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();
            dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
        }

        if (dayName.equals("Sunday")) {
            dayName = getResources().getString(R.string.sunday);
        } else if (dayName.equals("Monday")) {
            dayName = getResources().getString(R.string.monday);
        } else if (dayName.equals("Tuesday")) {
            dayName = getResources().getString(R.string.tuesday);
        } else if (dayName.equals("Wednesday")) {
            dayName = getResources().getString(R.string.wednesday);
        } else if (dayName.equals("Thursday")) {
            dayName = getResources().getString(R.string.thursday);
        } else if (dayName.equals("Friday")) {
            dayName = getResources().getString(R.string.friday);
        } else if (dayName.equals("Saturday")) {
            dayName = getResources().getString(R.string.saturday);
        }

        txtDate.setText(dayName + ", " +day + month + cal.get(Calendar.YEAR));
    }
}