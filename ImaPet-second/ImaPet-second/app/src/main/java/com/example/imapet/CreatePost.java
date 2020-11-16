package com.example.imapet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreatePost extends AppCompatActivity {

    public static final String CAPTION_KEY = "caption";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Button shareButton = (Button)findViewById(R.id.shareButton);
        CheckBox shareLocation = (CheckBox)findViewById(R.id.shareLocation);
        Button locationButton = (Button)findViewById(R.id.locationButton);

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
}