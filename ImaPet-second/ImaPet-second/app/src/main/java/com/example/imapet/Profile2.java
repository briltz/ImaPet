package com.example.imapet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Profile2 extends AppCompatActivity {

    public static final String NAME_KEY = "name";
    public static final String STATUS_KEY = "status";
    public static final String BIRTHDAY_KEY = "birthday";
    public static final String DESCRIPTION_KEY = "description";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Button useProfile = (Button)findViewById(R.id.useProfileButton);
        Button createProfile = (Button)findViewById(R.id.createProfileButton);

        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.myProfileName);
                EditText status = (EditText) findViewById(R.id.myStatus);
                EditText birthday = (EditText) findViewById(R.id.myBirthday);
                EditText description = (EditText) findViewById(R.id.myDescription);
                String nameText = name.getText().toString();
                String statusText = status.getText().toString();
                String birthdayText = birthday.getText().toString();
                String descriptionText = description.getText().toString();

                if(nameText.isEmpty() || statusText.isEmpty() || birthdayText.isEmpty()  || descriptionText.isEmpty()) { return; }
                Map<String, Object> profileToSave = new HashMap<String, Object>();
                profileToSave.put(NAME_KEY, nameText);
                profileToSave.put(STATUS_KEY, statusText);
                profileToSave.put(BIRTHDAY_KEY, birthdayText);
                profileToSave.put(DESCRIPTION_KEY, descriptionText);

                db.collection("Profiles")
                        .add(profileToSave)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Profile2.this, "Profile created successfully",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Profile2.this, ProfilePage.class);
                                intent.putExtra("PROFILE", nameText);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile2.this, "Error saving profile information",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        useProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText profileName = (EditText)findViewById(R.id.myProfileName);
                String profileNameText = profileName.getText().toString();
                db.collection("Profiles")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("name").equals(profileNameText)){
                                            Toast.makeText(Profile2.this, "Login successful",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Profile2.this, ProfilePage.class);
                                            intent.putExtra("PROFILE", profileNameText);
                                            startActivity(intent);
                                        }
                                    }
                                } else {
                                    Toast.makeText(Profile2.this, "Unable to reach login server",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }
}