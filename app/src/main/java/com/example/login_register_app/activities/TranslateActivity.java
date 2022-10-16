package com.example.login_register_app.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_register_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;

public class TranslateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button btnBackTranslate;

    TextView txtTranslateOrgLabel, txtTranslateTo, txtTranslateResultLabel;
    TextView txtTranslateOrgValue, txtTranslateResult;

    Spinner spnTranslateTxt;

    private ArrayList<String> myList;

    private Translator translatorEl, translatorDe, translatorEs;
    private Boolean boolEl = false, boolDe = false, boolEs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        btnBackTranslate = findViewById(R.id.btnBackTranslate);

        txtTranslateOrgLabel = findViewById(R.id.txtTranslateOrgLabel);
        txtTranslateOrgLabel.setPaintFlags(txtTranslateOrgLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtTranslateTo = findViewById(R.id.txtTranslateTo);
        txtTranslateTo.setPaintFlags(txtTranslateTo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtTranslateResultLabel = findViewById(R.id.txtTranslateResultLabel);
        txtTranslateResultLabel.setPaintFlags(txtTranslateResultLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtTranslateResultLabel.setVisibility(View.INVISIBLE);

        txtTranslateResult = findViewById(R.id.txtTranslateResult);
        txtTranslateResult.setVisibility(View.INVISIBLE);

        txtTranslateOrgValue = findViewById(R.id.txtTranslateOrgValue);

        spnTranslateTxt = findViewById(R.id.spnTranslateTxt);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.spn_langs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTranslateTxt.setAdapter(adapter);
        spnTranslateTxt.setOnItemSelectedListener(this);

        TranslatorOptions translatorOptionsEl =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.GREEK)
                        .build();
        translatorEl = Translation.getClient(translatorOptionsEl);

        TranslatorOptions translatorOptionsDe =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.GERMAN)
                        .build();
        translatorDe = Translation.getClient(translatorOptionsDe);

        TranslatorOptions translatorOptionsEs =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.SPANISH)
                        .build();
        translatorEs = Translation.getClient(translatorOptionsEs);

        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translatorEl.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolEl = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolEl = false;
                    }
                });

        translatorDe.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolDe = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolDe = false;
                    }
                });

        translatorEs.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolEs = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolEs = false;
                    }
                });


        myList = new ArrayList<>();
        myList = getIntent().getStringArrayListExtra("TRANSLATE_ACTV");
        txtTranslateOrgValue.setText(myList.get(1));


        /*if(myList.get(0).equals("img_label_list")) {
            Toast.makeText(TranslateActivity.this,
                    "I came from img label activity." +
                            "\nText: " + myList.get(1),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TranslateActivity.this,
                    "I came from txt rec activity." +
                            "\nText: " + myList.get(1),
                    Toast.LENGTH_SHORT).show();
        }*/

        btnBackTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(myList.get(0).equals("img_label_list")) {
                    intent = new Intent(TranslateActivity.this, ImgLabelingActivity.class);
                } else {
                    intent = new Intent(TranslateActivity.this, TxtRecognitionActivity.class);
                }
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
                if (myList.get(0).equals("img_label_list")){
                    if (selectedLang.equals(allLanguages[1])){
                        // translate text to greek...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        txtTranslateResult.clearComposingText();
                        translateText("el", myList.get(1));
                    } else if (selectedLang.equals(allLanguages[2])){
                        // translate text to german...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        txtTranslateResult.clearComposingText();
                        translateText("de", myList.get(1));
                    } else {
                        // translate text to spanish...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        txtTranslateResult.clearComposingText();
                        translateText("es", myList.get(1));
                    }
                } else {
                    if (selectedLang.equals(allLanguages[1])){
                        // translate text to greek...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        identifyLanguage(myList.get(1));
                        translateText("el", myList.get(1));
                    } else if (selectedLang.equals(allLanguages[2])){
                        // translate text to german...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        identifyLanguage(myList.get(1));
                        translateText("de", myList.get(1));
                    } else {
                        // translate text to spanish...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        identifyLanguage(myList.get(1));
                        translateText("es", myList.get(1));
                    }
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void translateText(String language, String text) {

        switch (language){
            case "el":
                if (boolEl){
                    translatorEl.translate(text)
                            .addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    txtTranslateResult.setText(s);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    txtTranslateResult.setText(e.toString());
                                }
                            });
                }
            case "de":
                if (boolDe){
                    translatorDe.translate(text)
                            .addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    txtTranslateResult.setText(s);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    txtTranslateResult.setText(e.toString());
                                }
                            });
                }
            case "es":
                if (boolEs){
                    translatorEs.translate(text)
                            .addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    txtTranslateResult.setText(s);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    txtTranslateResult.setText(e.toString());
                                }
                            });
                }
        }
    }

    private void identifyLanguage(String text) {

        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();
        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String languageCode) {
                        if (languageCode.equals("und")) {
                            Toast.makeText(TranslateActivity.this,
                                    "Can't identify language.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TranslateActivity.this,
                                    "Language: " + languageCode,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TranslateActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}