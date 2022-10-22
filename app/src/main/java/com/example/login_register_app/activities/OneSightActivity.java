package com.example.login_register_app.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_register_app.R;

public class OneSightActivity extends AppCompatActivity {

    ImageView imgOneSight;

    TextView txtOneSightNameLabel, txtOneSightCityLabel;
    TextView txtOneSightNameValue, txtOneSightCityValue;

    Button btnBackOneSight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_sight);

        imgOneSight = findViewById(R.id.imgOneSight);
        imgOneSight.setImageResource(getIntent().getIntExtra("SIGHT_IMG",0));

        txtOneSightNameLabel = findViewById(R.id.txtOneSightNameLabel);
        txtOneSightNameLabel.setPaintFlags(txtOneSightNameLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtOneSightCityLabel = findViewById(R.id.txtOneSightCityLabel);
        txtOneSightCityLabel.setPaintFlags(txtOneSightCityLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtOneSightNameValue = findViewById(R.id.txtOneSightNameValue);
        txtOneSightNameValue.setText(getIntent().getStringExtra("SIGHT_NAME"));
        txtOneSightCityValue = findViewById(R.id.txtOneSightCityValue);
        txtOneSightCityValue.setText(getIntent().getStringExtra("SIGHT_CITY"));

        btnBackOneSight = findViewById(R.id.btnBackOneSight);

        btnBackOneSight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OneSightActivity.this, SightsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}