package com.example.login_register_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.login_register_app.R;

public class SightsActivity extends AppCompatActivity {

    Button btnMap, btnFind, btnBack;

    TextView txtNearestSightLabel, txtNearestSightResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sights);

        btnMap = findViewById(R.id.btnMap);
        btnFind = findViewById(R.id.btnFind);
        btnBack = findViewById(R.id.btnBack);

        txtNearestSightLabel= findViewById(R.id.txtNearestSightLabel);
        txtNearestSightResult = findViewById(R.id.txtNearestSightResult);

        txtNearestSightLabel.setVisibility(View.INVISIBLE);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNearestSight();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SightsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findNearestSight () {

        if (txtNearestSightLabel.getVisibility() == View.INVISIBLE) {
            txtNearestSightLabel.setVisibility(View.VISIBLE);
            //txtNearestSightResult.setText("Akropoli");
        } else {
            txtNearestSightLabel.setVisibility(View.VISIBLE);
        }
    }
}