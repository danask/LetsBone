package com.mobile.letsbone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhotoUploadActivity extends AppCompatActivity {
    private Button btnChoosePhoto;
    private Button btnUploadPhoto;
    private ImageView imageViewUser;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser currentUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    String randomUID;

    public String getRandomUID() {
        return randomUID;
    }

    public void setRandomUID(String randomUID) {
        this.randomUID = randomUID;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);

        btnChoosePhoto = (Button) findViewById(R.id.btnChoosePhoto);
        btnUploadPhoto = (Button) findViewById(R.id.btnUploadPhoto);
        imageViewUser = (ImageView) findViewById(R.id.imgViewUser);

        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());

        ProgressDialog progressDialog;

        btnUploadPhoto.setVisibility(View.INVISIBLE);

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewUser.setImageBitmap(bitmap);
                btnChoosePhoto.setVisibility(View.INVISIBLE);
                btnUploadPhoto.setVisibility(View.VISIBLE);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

//        StorageReference ref = storageReference.child("Users").child(filePath.getLastPathSegment());
//        String profilePicURL = filePath.getLastPathSegment();
//
//        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                final Uri imageURL = taskSnapshot.getD
//            }
//        });
        randomUID = UUID.randomUUID().toString();
        final StorageReference ref = storageReference.child("images/" + randomUID);
        UploadTask uploadTask = ref.putFile(filePath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri imageURL = task.getResult();
                    //TODO: Get the UID of the photo in Storage and put it as the value of Photo in the DB
                    //databaseReference.child("Photo").setValue(imageURL.toString());
                    databaseReference.child("Photo").setValue(randomUID);
                    progressDialog.dismiss();
                    finish();
                }
            }
        });
    }

    //    private void chooseImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageViewUser.setImageBitmap(bitmap);
//                btnChoosePhoto.setVisibility(View.INVISIBLE);
//                btnUploadPhoto.setVisibility(View.VISIBLE);
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void uploadImage() {
//        if(filePath != null) {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
//            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressDialog.dismiss();
//                    currentUserString = auth.getCurrentUser().getUid();
//                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserString);
//                    Map userInfo = new HashMap<>();
//                    userInfo.put("Photo", filePath.toString());
//                    //databaseReference.updateChildren(userInfo);
//                    databaseReference.child(currentUser.getUid()).updateChildren(userInfo);
//                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Upload failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 + taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
//                }
//            });
//        }
//    }
}
