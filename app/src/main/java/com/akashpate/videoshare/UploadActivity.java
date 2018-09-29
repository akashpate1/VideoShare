package com.akashpate.videoshare;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class UploadActivity extends AppCompatActivity {

    Button uploadBtn;
    VideoView videoView;
    StorageReference videoRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadBtn = findViewById(R.id.uploadBtn);
        videoView = findViewById(R.id.videoPreview);


        Intent intent = getIntent();

        final String fileUrl = (intent.getStringExtra("fileUrl"));
        videoView.setVideoPath(fileUrl);
        videoView.start();

        //Firebase storage initialization
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        //Firebase database initialization
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


        //Creating new storage reference
        final StorageReference storageRef = storage.getReference();
        //Creating new database reference
        final DatabaseReference videoDbRef = firebaseDatabase.getReference();

        //Check for READ_EXTERNAL_STORAGE Permission
        if (ContextCompat.checkSelfPermission(UploadActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }




        //Upload Video
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(UploadActivity.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                Uri file = Uri.parse(fileUrl);
                videoRef = storageRef.child("videos/"+file.getLastPathSegment());


                final UploadTask uploadTask = videoRef.putFile(file);




                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        //Uri uri = videoRef.getDownloadUrl().getResult();
                        Toast.makeText(UploadActivity.this,"Upload Success",Toast.LENGTH_SHORT).show();
                    }
                });
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadActivity.this,"Upload Failed!",Toast.LENGTH_SHORT).show();
                    }
                });


                videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(UploadActivity.this,uri.toString(),Toast.LENGTH_LONG).show();
                        videoDbRef.child("url").push().setValue(uri.toString());
                    }
                });


            }

        });




    }


}
