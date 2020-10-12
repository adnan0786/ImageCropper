package com.example.imagecropper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btnPickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.img);
        btnPickImage = findViewById(R.id.btnPick);

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStorageOk())
                    pickImage();
                else requestStoragePermission();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private boolean isStorageOk() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                pickImage();
            } else Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    private void pickImage() {
        CropImage.activity()
                .setActivityTitle("Crop Image")
                .setActivityMenuIconColor(Color.GREEN)
                .setAllowFlipping(false)
                .setAllowRotation(false)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setGuidelinesThickness(20.0f)
                .setGuidelinesColor(Color.GREEN)
                .setAutoZoomEnabled(false)
                .setBorderLineThickness(20.0f)
                .setBorderLineColor(Color.GREEN)
                .setBorderCornerColor(Color.RED)
                .setBorderCornerThickness(20.0f)
                .setCropMenuCropButtonIcon(R.drawable.ic_crop)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }
}