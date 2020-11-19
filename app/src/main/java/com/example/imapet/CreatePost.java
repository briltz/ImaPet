package com.example.imapet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreatePost extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 9;
    private static final int CAMERA_REQUEST = 11;

    public static final String CAPTION_KEY = "caption";

    ImageView postPic;
    Button cameraPic;
    Button galleryPic;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        postPic = (ImageView) findViewById(R.id.iv_post_image);

        Button shareButton = (Button)findViewById(R.id.shareButton);
        CheckBox shareLocation = (CheckBox)findViewById(R.id.shareLocation);
        Button locationButton = (Button)findViewById(R.id.locationButton);

        cameraPic = (Button) findViewById(R.id.btn_camera);
        takeAPhoto();

        galleryPic = (Button) findViewById(R.id.btn_gallery);
        uploadPhoto();

        locationButton.setVisibility(View.GONE);

        shareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shareLocation.isChecked()){
                    locationButton.setVisibility(View.VISIBLE);
                }
                else {
                    locationButton.setVisibility(View.GONE);
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText caption = (EditText)findViewById(R.id.caption);
                String captionText = caption.getText().toString();

                if(captionText.isEmpty()) { return; }
                Map<String, Object> postToSave = new HashMap<String, Object>();
                postToSave.put(CAPTION_KEY, captionText);

                db.collection("Posts")
                        .add(postToSave)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(CreatePost.this, "Post created successfully",
                                        Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CreatePost.this, ProfilePage.class));
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

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            postPic.setImageBitmap(bitmap);
        }
    }
}