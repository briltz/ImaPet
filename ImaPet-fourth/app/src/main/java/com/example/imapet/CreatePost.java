package com.example.imapet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreatePost extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 9;
    private static final int CAMERA_REQUEST = 11;

    public static final String CAPTION_KEY = "caption";
    public static final String IMAGE_KEY = "image";
    public static final String AUTHOR_KEY = "author";
    public static final String LOCATION_KEY = "location";

    ImageView postPic;
    Button cameraPic;
    Button galleryPic;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    String profile;
    String profileName;

    public Uri postUri;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        postPic = (ImageView) findViewById(R.id.iv_post_image);

        Button shareButton = (Button) findViewById(R.id.shareButton);
        CheckBox shareLocation = (CheckBox) findViewById(R.id.shareLocation);
        Button locationButton = (Button) findViewById(R.id.locationButton);

        cameraPic = (Button) findViewById(R.id.btn_camera);
        takeAPhoto();

        galleryPic = (Button) findViewById(R.id.btn_gallery);
        uploadPhoto();

        profile = getIntent().getStringExtra("PROFILE");
        DocumentReference docRef = db.collection("Profiles").document(profile);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    profileName = document.get("name").toString();
                }
            }
        });

        locationButton.setVisibility(View.GONE);

        shareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareLocation.isChecked()) {
                    locationButton.setVisibility(View.VISIBLE);
                } else {
                    locationButton.setVisibility(View.GONE);
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String randomKey = UUID.randomUUID().toString();
                StorageReference riversRef = storageReference.child("images/" + randomKey);

                riversRef.putFile(postUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });

                EditText caption = (EditText) findViewById(R.id.caption);
                String captionText = caption.getText().toString();

                if (captionText.isEmpty()) {
                    return;
                }
                Map<String, Object> postToSave = new HashMap<String, Object>();
                postToSave.put(CAPTION_KEY, captionText);
                postToSave.put(IMAGE_KEY, randomKey);
                postToSave.put(AUTHOR_KEY, profileName);

                if (shareLocation.isChecked()) {
                    postToSave.put(LOCATION_KEY, "location");
                }
                else {
                    postToSave.put(LOCATION_KEY, "no location");
                }

                db.collection("Posts")
                        .add(postToSave)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(CreatePost.this, "Post created successfully",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CreatePost.this, ProfilePage.class);
                                intent.putExtra("PROFILE", profile);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreatePost.this, "Error creating post",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    //Methods for camera
    public void takeAPhoto() {
        cameraPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent();
                camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA_REQUEST);
            }
        });
    }

    public void uploadPhoto() {
        galleryPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri chosenPicture = data.getData();
            postPic.setImageURI(chosenPicture);
            postUri = chosenPicture;

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            postPic.setImageBitmap(bitmap);
            postUri = getImageUri(getApplicationContext(), bitmap);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}