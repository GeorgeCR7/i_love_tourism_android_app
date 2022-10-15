package com.example.login_register_app.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_register_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TxtRecognitionActivity extends AppCompatActivity {

    Button btnTxtRecLoadImg, btnTxtRecDetect, btnTxtRecTranslate, btnBackTxtRec;

    TextView txtYourImageRec, txtRecResult;

    ImageView imageTxtRec;

    private String strUri;
    private ArrayList<String> txtRecList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_recognition);

        btnTxtRecLoadImg = findViewById(R.id.btnTxtRecLoadImg);
        btnTxtRecDetect = findViewById(R.id.btnTxtRecDetect);
        btnTxtRecDetect.setVisibility(View.INVISIBLE);
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

        strUri = "";

        btnTxtRecLoadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtYourImageRec.setVisibility(View.VISIBLE);
                imageTxtRec.setVisibility(View.VISIBLE);
                btnTxtRecDetect.setVisibility(View.VISIBLE);
                btnTxtRecTranslate.setVisibility(View.VISIBLE);
                mGetContent.launch("image/*");
            }
        });

        btnTxtRecDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(strUri);
                extractTextFromUri(getApplicationContext(), uri);
            }
        });

        btnTxtRecTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TxtRecognitionActivity.this, TranslateActivity.class);
                txtRecList.add(txtRecResult.getText().toString());
                intent.putStringArrayListExtra("TRANSLATE_ACTV", txtRecList);
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

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    imageTxtRec.setImageURI(uri);
                    strUri = uri.toString();
                }
            }
    );

    private void extractTextFromUri(Context context, Uri uri) {

        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        try {
            InputImage image = InputImage.fromFilePath(context, uri);
            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text text) {
                                    txtRecResult.setText(text.getText());
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {}
                                    }
                            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}