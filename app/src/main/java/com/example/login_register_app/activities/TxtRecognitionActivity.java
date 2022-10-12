package com.example.login_register_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login_register_app.R;

import java.util.ArrayList;

public class TxtRecognitionActivity extends AppCompatActivity {

    Button btnTxtRecLoadImg, btnTxtRecTranslate, btnBackTxtRec;

    TextView txtYourImageRec, txtRecResult;

    ImageView imageTxtRec;

    private ArrayList<String> txtRecList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_recognition);

        btnTxtRecLoadImg = findViewById(R.id.btnTxtRecLoadImg);
        btnTxtRecTranslate = findViewById(R.id.btnTxtRecTranslate);
        btnTxtRecTranslate.setVisibility(View.INVISIBLE);
        btnBackTxtRec = findViewById(R.id.btnBackTxtRec);

        txtYourImageRec = findViewById(R.id.txtYourImageRec);
        txtYourImageRec.setVisibility(View.INVISIBLE);
        txtRecResult = findViewById(R.id.txtRecResult);

        imageTxtRec = findViewById(R.id.imageTxtRec);
        imageTxtRec.setVisibility(View.INVISIBLE);

        txtRecList = new ArrayList<>();
        txtRecList.add("txt_rec");

        btnTxtRecTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TxtRecognitionActivity.this, TranslateActivity.class);
                txtRecList.add(txtRecResult.getText().toString());
                intent.putStringArrayListExtra("txt_rec_list", txtRecList);
                startActivity(intent);
                finish();
            }
        });

        btnBackTxtRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TxtRecognitionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}