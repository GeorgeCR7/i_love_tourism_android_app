package com.example.login_register_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.login_register_app.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    RadioGroup langGroup;
    RadioButton langEng, langGr, langGerm, langSpa;
    TextView langText;

    Button btnOK;
    //RadioButton choosenLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        langGroup = findViewById(R.id.langGroup);
        langEng = findViewById(R.id.langEng);
        langGr = findViewById(R.id.langGr);
        langGerm = findViewById(R.id.langGerm);
        langSpa = findViewById(R.id.langSpa);
        langText = findViewById(R.id.langText);
        btnOK = findViewById(R.id.btnOK);

        // Set listener to radio group of languages.
        // TODO: Add French language and more...
        langGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // Check condition.
                switch (i) {
                    case R.id.langEng:
                        String language = "en";
                        setLocale(language);
                        break;
                    case R.id.langGr:
                        setLocale("el");
                        break;
                    case R.id.langGerm:
                        setLocale("de");
                        break;
                    case R.id.langSpa:
                        setLocale("es");
                        break;
                }
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LanguageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setLocale(String language) {
        // Initialize resources.
        Resources resources = getResources();
        // Initialize metrics.
        DisplayMetrics metrics = resources.getDisplayMetrics();
        // Initialize configuration.
        Configuration configuration = resources.getConfiguration();
        // Initialize locale.
        configuration.locale = new Locale(language);
        // Update configuration.
        resources.updateConfiguration(configuration, metrics);
        // Notify configuration.
        onConfigurationChanged(configuration);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Set strings from resources.
        langText.setText(R.string.select_your_language);
        langEng.setText(R.string.english);
        langGr.setText(R.string.greek);
        langGerm.setText(R.string.german);
        langSpa.setText(R.string.spanish);
    }
}