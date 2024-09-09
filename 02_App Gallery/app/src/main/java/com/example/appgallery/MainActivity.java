package com.example.appgallery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public ActivityResultLauncher<Intent> someActivityResultLauncher = null;
    public ActivityResultLauncher<Intent> someActivityResultLauncher2 = null;
    public static int RC_PHOTO_PICKER = 0;
    public Uri uriPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button abrirGale = findViewById(R.id.abrirGaleria);
        Button abrirCamara = findViewById(R.id.camaraButton);


        this.someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            ImageView imageView = findViewById(R.id.img);
                            imageView.setImageURI(uri);
                        }
                    }
                });
        abrirGale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSomeActivityForResult(null);
            }
        });


        this.someActivityResultLauncher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            ImageView imagen = findViewById(R.id.img);
                            imagen.setImageURI(uriPhoto);
                        }
                    }
                });
        abrirCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSomeActivityForResult2(null);
            }
        });

    }
    public void openSomeActivityForResult(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        someActivityResultLauncher.launch(intent);
    }

    public void openSomeActivityForResult2(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                uriPhoto = FileProvider.getUriForFile(this, "com.example.appgallery.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);
            }
        someActivityResultLauncher2.launch(takePictureIntent);
    }

    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}