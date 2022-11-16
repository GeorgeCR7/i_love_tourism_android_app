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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_register_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;

public class TranslateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button btnBackTranslate;

    TextView txtTranslateOrgLabel, txtTranslateTo, txtTranslateResultLabel;
    TextView txtLanguageLabel, txtLanguageValue;
    TextView txtTranslateOrgValue, txtTranslateResult;

    Spinner spnTranslateTxt;

    private ArrayList<String> myList;

    private Translator translatorEn, translatorEl, translatorDe, translatorEs;
    private Boolean boolEn = false, boolEl = false, boolDe = false, boolEs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        myList = new ArrayList<>();
        myList = getIntent().getStringArrayListExtra("TRANSLATE_ACTV");

        spnTranslateTxt = findViewById(R.id.spnTranslateTxt);
        createSpinner(myList.get(2));

        btnBackTranslate = findViewById(R.id.btnBackTranslate);

        txtTranslateOrgLabel = findViewById(R.id.txtTranslateOrgLabel);
        txtTranslateOrgLabel.setPaintFlags(txtTranslateOrgLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtTranslateTo = findViewById(R.id.txtTranslateTo);
        txtTranslateTo.setPaintFlags(txtTranslateTo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtLanguageLabel = findViewById(R.id.txtLanguageLabel);
        txtLanguageLabel.setPaintFlags(txtLanguageLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtLanguageValue = findViewById(R.id.txtLanguageValue);
        txtLanguageValue.setText(setLanguageValue(myList.get(2)));

        txtTranslateResultLabel = findViewById(R.id.txtTranslateResultLabel);
        txtTranslateResultLabel.setPaintFlags(txtTranslateResultLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtTranslateResultLabel.setVisibility(View.INVISIBLE);

        txtTranslateResult = findViewById(R.id.txtTranslateResult);
        txtTranslateResult.setVisibility(View.INVISIBLE);

        txtTranslateOrgValue = findViewById(R.id.txtTranslateOrgValue);
        txtTranslateOrgValue.setText(myList.get(1));

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
        String[] allLanguages =
                { getResources().getString(R.string.english),
                getResources().getString(R.string.greek),
                getResources().getString(R.string.german),
                getResources().getString(R.string.spanish) };



        /*if (!selectedLang.equals("")){
            if (extras!= null){
                if (myList.get(0).equals("img_label_list")){
                    if (selectedLang.equals(allLanguages[1])){
                        // translate text to greek...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        translateText("el", myList.get(1));
                    } else if (selectedLang.equals(allLanguages[2])){
                        // translate text to german...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        translateText("de", myList.get(1));
                    } else {
                        // translate text to spanish...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        translateText("es", myList.get(1));
                    }
                } else {
                    if (selectedLang.equals(allLanguages[1])){
                        // translate text to greek...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        translateText("el", myList.get(1));
                    } else if (selectedLang.equals(allLanguages[2])){
                        // translate text to german...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        translateText("de", myList.get(1));
                    } else {
                        // translate text to spanish...
                        txtTranslateResultLabel.setVisibility(View.VISIBLE);
                        txtTranslateResult.setVisibility(View.VISIBLE);
                        translateText("es", myList.get(1));
                    }
                }
            }
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void createSpinner(String langCode) {

        ArrayAdapter<CharSequence> adapter = null;

        if(langCode.contains("en")){
            adapter = ArrayAdapter.createFromResource(this, R.array.langs_noEn, android.R.layout.simple_spinner_item);
        } else if(langCode.contains("de")) {
            adapter = ArrayAdapter.createFromResource(this, R.array.langs_noDe, android.R.layout.simple_spinner_item);
        } else if(langCode.contains("el")){
            adapter = ArrayAdapter.createFromResource(this, R.array.langs_noEl, android.R.layout.simple_spinner_item);
        } else if(langCode.contains("es")){
            adapter = ArrayAdapter.createFromResource(this, R.array.langs_noEs, android.R.layout.simple_spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(this, R.array.langs_no, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTranslateTxt.setAdapter(adapter);
        spnTranslateTxt.setOnItemSelectedListener(this);
    }

    private void createTranslator(String langCode) {

        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

    }

    private void translateText(String langCode, String text) {

        if (langCode.equals("el")){
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
        } else if (langCode.equals("de")){
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
        } else if (langCode.equals("es")){
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

    private String setLanguageValue(String langCode){

        String langLabel;

        if(langCode.contains("en")){
            langLabel = getResources().getString(R.string.english);
        } else if(langCode.contains("de")) {
            langLabel = getResources().getString(R.string.german);
        } else if(langCode.contains("el")){
            langLabel = getResources().getString(R.string.greek);
        } else if(langCode.contains("es")){
            langLabel = getResources().getString(R.string.spanish);
        } else {
            langLabel = getResources().getString(R.string.lang_no_support);
            txtTranslateTo.setVisibility(View.INVISIBLE);
            spnTranslateTxt.setVisibility(View.INVISIBLE);
        }

        return langLabel;

    }

    /*private void identifyLanguage(String text) {

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
    }*/
}