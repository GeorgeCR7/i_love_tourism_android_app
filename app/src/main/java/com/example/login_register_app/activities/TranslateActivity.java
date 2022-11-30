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

    // Translator objects.
    private Translator translatorEnEl, translatorEnDe, translatorEnEs;
    private Boolean boolEnEl = false, boolEnDe = false, boolEnEs = false;

    private Translator translatorElEn, translatorElDe, translatorElEs;
    private Boolean boolElEn = false, boolElDe = false, boolElEs = false;

    private Translator translatorDeEn, translatorDeEl, translatorDeEs;
    private Boolean boolDeEn = false, boolDeEl = false, boolDeEs = false;

    private Translator translatorEsEn, translatorEsEl, translatorEsDe;
    private Boolean boolEsEn = false, boolEsEl = false, boolEsDe = false;

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

        createTranslators();

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
        
        if (!selectedLang.equals("")) {
            if (extras != null) {
                if (myList.get(2).contains("en") && selectedLang.equals(getResources().getString(R.string.greek))) {
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("en-el", myList.get(1));
                } else if (myList.get(2).contains("en") && selectedLang.equals(getResources().getString(R.string.german))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("en-de", myList.get(1));
                } else if (myList.get(2).contains("en") && selectedLang.equals(getResources().getString(R.string.spanish))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("en-es", myList.get(1));

                } else if (myList.get(2).contains("de") && selectedLang.equals(getResources().getString(R.string.english))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("de-en", myList.get(1));
                } else if (myList.get(2).contains("de") && selectedLang.equals(getResources().getString(R.string.greek))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("de-el", myList.get(1));
                } else if (myList.get(2).contains("de") && selectedLang.equals(getResources().getString(R.string.spanish))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("de-es", myList.get(1));

                } else if (myList.get(2).contains("el") && selectedLang.equals(getResources().getString(R.string.english))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("el-en", myList.get(1));
                } else if (myList.get(2).contains("el") && selectedLang.equals(getResources().getString(R.string.german))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("el-de", myList.get(1));
                } else if (myList.get(2).contains("el") && selectedLang.equals(getResources().getString(R.string.spanish))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("el-es", myList.get(1));

                } else if (myList.get(2).contains("es") && selectedLang.equals(getResources().getString(R.string.english))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("es-en", myList.get(1));
                } else if (myList.get(2).contains("es") && selectedLang.equals(getResources().getString(R.string.greek))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("es-el", myList.get(1));
                } else if (myList.get(2).contains("es") && selectedLang.equals(getResources().getString(R.string.german))){
                    txtTranslateResultLabel.setVisibility(View.VISIBLE);
                    txtTranslateResult.setVisibility(View.VISIBLE);
                    translateText("es-de", myList.get(1));
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void createSpinner(String langCode) {

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

    private void createTranslators() {

       DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        // English --> Greek.
        TranslatorOptions translatorOptionsEnEl =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.GREEK)
                        .build();
        translatorEnEl = Translation.getClient(translatorOptionsEnEl);

        translatorEnEl.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolEnEl = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolEnEl = false;
                    }
                });

        // English --> German.
        TranslatorOptions translatorOptionsEnDe =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.GERMAN)
                        .build();
        translatorEnDe = Translation.getClient(translatorOptionsEnDe);

        translatorEnDe.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolEnDe = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolEnDe = false;
                    }
                });

        // English --> Spanish.
        TranslatorOptions translatorOptionsEnEs =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.SPANISH)
                        .build();
        translatorEnEs = Translation.getClient(translatorOptionsEnEs);

        translatorEnEs.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolEnEs = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolEnEs = false;
                    }
                });

        // German --> English.
        TranslatorOptions translatorOptionsDeEn =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.GERMAN)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
        translatorDeEn = Translation.getClient(translatorOptionsDeEn);

        translatorDeEn.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolDeEn = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolDeEn = false;
                    }
                });

        // German --> Greek.
        TranslatorOptions translatorOptionsDeEl =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.GERMAN)
                        .setTargetLanguage(TranslateLanguage.GREEK)
                        .build();
        translatorDeEl = Translation.getClient(translatorOptionsDeEl);

        translatorDeEl.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolDeEl = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolDeEl = false;
                    }
                });

        // German --> Spanish.
        TranslatorOptions translatorOptionsDeEs =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.GERMAN)
                        .setTargetLanguage(TranslateLanguage.SPANISH)
                        .build();
        translatorDeEs = Translation.getClient(translatorOptionsDeEs);

        translatorDeEs.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolDeEs = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolDeEs = false;
                    }
                });

        // Greek --> English.
        TranslatorOptions translatorOptionsElEn =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.GREEK)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
        translatorElEn = Translation.getClient(translatorOptionsElEn);

        translatorElEn.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolElEn = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolElEn = false;
                    }
                });

        // Greek --> German.
        TranslatorOptions translatorOptionsElDe =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.GREEK)
                        .setTargetLanguage(TranslateLanguage.GERMAN)
                        .build();
        translatorElDe = Translation.getClient(translatorOptionsElDe);

        translatorElDe.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolElDe = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolElDe = false;
                    }
                });

        // Greek --> Spanish.
        TranslatorOptions translatorOptionsElEs =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.GREEK)
                        .setTargetLanguage(TranslateLanguage.SPANISH)
                        .build();
        translatorElEs = Translation.getClient(translatorOptionsElEs);

        translatorElEs.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolElEs = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolElEs = false;
                    }
                });


        // Spanish --> English.
        TranslatorOptions translatorOptionsEsEn =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.SPANISH)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
        translatorEsEn = Translation.getClient(translatorOptionsEsEn);

        translatorEsEn.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolEsEn = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolEsEn = false;
                    }
                });

        // Spanish --> Greek.
        TranslatorOptions translatorOptionsEsEl =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.SPANISH)
                        .setTargetLanguage(TranslateLanguage.GREEK)
                        .build();
        translatorEsEl = Translation.getClient(translatorOptionsEsEl);

        translatorEsEl.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolEsEl = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolEsEl = false;
                    }
                });

        // Spanish --> German.
        TranslatorOptions translatorOptionsEsDe =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.SPANISH)
                        .setTargetLanguage(TranslateLanguage.GERMAN)
                        .build();
        translatorEsDe = Translation.getClient(translatorOptionsEsDe);

        translatorEsDe.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolEsDe = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        boolEsDe = false;
                    }
                });
    }

    private void translateText(String translatorType, String text) {

        if (translatorType.equals("en-el")){
            if (boolEnEl){
                translatorEnEl.translate(text)
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
        } else if (translatorType.equals("en-de")){
            if (boolEnDe){
                translatorEnDe.translate(text)
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
        } else if (translatorType.equals("en-es")){
            if (boolEnEs){
                translatorEnEs.translate(text)
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
        } else if (translatorType.equals("de-en")){
            if (boolDeEn){
                translatorEnEs.translate(text)
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
        } else if (translatorType.equals("de-el")){
            if (boolDeEl){
                translatorEnEs.translate(text)
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
        } else if (translatorType.equals("de-es")){
            if (boolDeEs){
                translatorEnEs.translate(text)
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
        } else if (translatorType.equals("el-en")){
            if (boolElEn){
                translatorEnEs.translate(text)
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
        } else if (translatorType.equals("el-de")){
            if (boolElDe){
                translatorEnEs.translate(text)
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
        } else if (translatorType.equals("el-es")){
            if (boolElEs){
                translatorEnEs.translate(text)
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
        } else if (translatorType.equals("es-en")){
            if (boolEsEn){
                translatorEnEs.translate(text)
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
        } else if (translatorType.equals("es-el")){
            if (boolEsEl){
                translatorEnEs.translate(text)
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
        } else if (translatorType.equals("es-de")){
            if (boolEsDe){
                translatorEnEs.translate(text)
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
}