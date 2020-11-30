package com.example.imapet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Profile2 extends AppCompatActivity {

    public static final String NAME_KEY = "name";
    public static final String STATUS_KEY = "status";
    public static final String BIRTHDAY_KEY = "birthday";
    public static final String DESCRIPTION_KEY = "description";
    public static final String PICTURE_KEY = "picture";
    public static final String ACCOUNT_KEY = "account";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Button useProfile = (Button)findViewById(R.id.useProfileButton);
        Button createProfile = (Button)findViewById(R.id.createProfileButton);
        Button enterBirthday = (Button) findViewById(R.id.btn_birthday);
        birthday = (EditText) findViewById(R.id.myBirthday);

        String account = getIntent().getStringExtra("ACCOUNT");

        enterBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Profile2.this, d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.myProfileName);
                EditText status = (EditText) findViewById(R.id.myStatus);
                EditText description = (EditText) findViewById(R.id.myDescription);
                EditText birthday = (EditText) findViewById(R.id.myBirthday);
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
                profileToSave.put(PICTURE_KEY, "");
                profileToSave.put(ACCOUNT_KEY, account);

                db.collection("Profiles")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                boolean found = false;
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("name").equals(nameText)){
                                            Toast.makeText(Profile2.this, "A profile with that name already exists", Toast.LENGTH_LONG).show();
                                            found = true;
                                        }
                                    }
                                    if(!found){
                                        db.collection("Profiles")
                                                .add(profileToSave)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(Profile2.this, "Profile created successfully",
                                                                Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(Profile2.this, ProfilePage.class);
                                                        intent.putExtra("PROFILE", documentReference.getId());
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
                                } else {
                                    Toast.makeText(Profile2.this, "Unable to reach login server",
                                            Toast.LENGTH_LONG).show();
                                }
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
                                        if(document.get("name").equals(profileNameText) && document.get("account").equals(account)){
                                            Toast.makeText(Profile2.this, "Login successful", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Profile2.this, ProfilePage.class);
                                            intent.putExtra("PROFILE", document.getId());
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

    Calendar c = Calendar.getInstance();
    DateFormat fmtDate = DateFormat.getDateInstance();
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            birthday.setText(fmtDate.format(c.getTime()));
        }
    };
}