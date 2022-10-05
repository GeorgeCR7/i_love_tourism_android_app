package com.example.login_register_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.login_register_app.R;

public class TranslateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button btnBackTranslate;

    TextView txtTranslateResult;

    Spinner spnTranslateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        btnBackTranslate = findViewById(R.id.btnBackTranslate);

        txtTranslateResult = findViewById(R.id.txtTranslateResult);
        txtTranslateResult.setVisibility(View.INVISIBLE);

        spnTranslateTxt = findViewById(R.id.spnTranslateTxt);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.spn_langs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTranslateTxt.setAdapter(adapter);
        spnTranslateTxt.setOnItemSelectedListener(this);

        btnBackTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TranslateActivity.this, ImgLabelingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String selectedLang = adapterView.getItemAtPosition(position).toString();
        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
            txtTranslateResult.setVisibility(View.VISIBLE);
            txtTranslateResult.setText(extras.getString("TXT_TO_TRANSLATE"));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}