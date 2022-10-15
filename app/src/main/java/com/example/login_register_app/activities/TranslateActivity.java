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
import android.widget.Toast;

import com.example.login_register_app.R;

import java.util.ArrayList;

public class TranslateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button btnBackTranslate;

    TextView txtTranslateResult;

    Spinner spnTranslateTxt;

    ArrayList<String> myList;

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

        myList = new ArrayList<>();
        myList = getIntent().getStringArrayListExtra("TRANSLATE_ACTV");

        if(myList.get(0).equals("img_label_list")) {
            Toast.makeText(TranslateActivity.this,
                    "I came from img label activity.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TranslateActivity.this,
                    "I came from txt rec activity.",
                    Toast.LENGTH_SHORT).show();
        }

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

        // Get the selected language from the spinner.
        String selectedLang = adapterView.getItemAtPosition(position).toString();
        Bundle extras = getIntent().getExtras();
        String[] allLanguages = getResources().getStringArray(R.array.spn_langs);








        if (!selectedLang.equals("")){
            if (extras!= null){
                if (selectedLang.equals(allLanguages[1])){
                    // translate text to greek...
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    txtTranslateResult.setText(translateText("el", extras.getString("TXT_TO_TRANSLATE")));
                } else if (selectedLang.equals(allLanguages[2])){
                    // translate text to german...
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    txtTranslateResult.setText(translateText("de", extras.getString("TXT_TO_TRANSLATE")));
                } else {
                    // translate text to spanish...
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    txtTranslateResult.setText(translateText("es", extras.getString("TXT_TO_TRANSLATE")));
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private String translateText(String language, String text) {

        /*switch (language){
            case "el":
                return "Greek";
            case "de":
                return "German";
            case "es":
                return "Spanish";
        }*/

        return null;
    }
}