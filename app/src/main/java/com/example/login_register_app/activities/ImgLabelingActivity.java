package com.example.login_register_app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_register_app.R;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

public class ImgLabelingActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    private static final int CAMERA_PERMISSION_CODE = 223;
    private static final int READ_STORAGE_PERMISSION_CODE = 144;
    private static final int WRITE_STORAGE_PERMISSION_CODE = 144;

    Button btnLoadImage, btnBackImgLbl;

    TextView txtYourImage, txtImgLblResult;

    ImageView myImage;
    InputImage inputImage;
    ImageLabeler labeler;

    ActivityResultLauncher<Intent> cameraLauncher;
    ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_labeling);

        btnLoadImage = findViewById(R.id.btnLoadImage);
        btnBackImgLbl = findViewById(R.id.btnBackImgLbl);

        txtImgLblResult = findViewById(R.id.txtImgLblResult);
        txtYourImage = findViewById(R.id.txtYourImage);
        txtYourImage.setVisibility(View.INVISIBLE);

        myImage = findViewById(R.id.myImage);
        myImage.setVisibility(View.INVISIBLE);

        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtYourImage.getVisibility() == View.INVISIBLE) {
                    txtYourImage.setVisibility(View.VISIBLE);
                } else {
                    txtYourImage.setVisibility(View.VISIBLE);
                }
                if (myImage.getVisibility() == View.INVISIBLE) {
                    myImage.setVisibility(View.VISIBLE);
                } else {
                    myImage.setVisibility(View.VISIBLE);
                }

                String [] options = {getResources().getString(R.string.load_from_camera),
                        getResources().getString(R.string.load_from_gallery)};

                // Create an alert dialog when button clicked.
                // Give the user the options, to take a picture from camera or upload from device's storage fro labeling.
                AlertDialog.Builder builder = new AlertDialog.Builder(ImgLabelingActivity.this);
                builder.setTitle(getResources().getString(R.string.load_image));
                builder.setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraLauncher.launch(cameraIntent);
                    } else {
                        Intent storageIntent = new Intent();
                        storageIntent.setType("image/*");
                        storageIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryLauncher.launch(storageIntent);
                    }
                }).show();
            }
        });

        btnBackImgLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImgLabelingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Using on device-image default labeler.
        labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    try {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        myImage.setImageBitmap(photo);
                        inputImage = InputImage.fromBitmap(photo, 0);
                        processImage();
                    }catch (Exception e) {
                        Log.d(TAG, "camera - onActivityResult: " + e.getMessage());
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    try {
                        inputImage = InputImage.fromFilePath(ImgLabelingActivity.this, data.getData());
                        myImage.setImageURI(data.getData());
                        processImage();
                    }catch (Exception e) {
                        Log.d(TAG, "gallery - onActivityResult: " + e.getMessage());
                    }
                }
        );
    }

    // This method process the image and create the final labeling.
    private void processImage() {

        labeler.process(inputImage)
                .addOnSuccessListener(imageLabels -> {
                    String result = "";
                    for (ImageLabel label : imageLabels) {
                        // Get the description of the image via labeling.
                        result += "\n" + label.getText();
                    }
                    // Show the result in the screen.
                    txtImgLblResult.setText(result);
                })
                .addOnFailureListener(e -> Log.d(TAG, "processImage - onFailure: " + e.getMessage()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }

    public void checkPermission (String permission, int requestCode) {

        //Checking if permission granted or not.
        if (ContextCompat.checkSelfPermission(ImgLabelingActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Take permission.
            ActivityCompat.requestPermissions(ImgLabelingActivity.this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(ImgLabelingActivity.this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }else{
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_PERMISSION_CODE);
            }
        }else if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(ImgLabelingActivity.this, "Storage permission denied", Toast.LENGTH_LONG).show();
            }else{
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_STORAGE_PERMISSION_CODE);
            }
        }else if (requestCode == WRITE_STORAGE_PERMISSION_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(ImgLabelingActivity.this, "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}