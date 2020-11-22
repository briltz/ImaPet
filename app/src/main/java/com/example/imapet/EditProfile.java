package com.example.imapet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    public static final String NAME_KEY = "name";
    public static final String STATUS_KEY = "status";
    public static final String BIRTHDAY_KEY = "birthday";
    public static final String DESCRIPTION_KEY = "description";
    public static final String PICTURE_KEY = "picture";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText birthday;
    String pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        String profile = getIntent().getStringExtra("PROFILE");

        Button updateProfile = (Button)findViewById(R.id.updateProfile);
        Button editBirthdayButton= (Button) findViewById(R.id.editBirthdayButton);
        birthday = (EditText) findViewById(R.id.editBirthday);

        TextView editName = (TextView)findViewById(R.id.editUsername);
        EditText editStatus = (EditText)findViewById(R.id.editStatus);
        EditText editBirthday = (EditText)findViewById(R.id.editBirthday);
        EditText editDescription = (EditText)findViewById(R.id.editDescription);

        DocumentReference docRef = db.collection("Profiles").document(profile);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        editName.setText(document.get("name").toString());
                        editStatus.setText(document.get("status").toString());
                        editBirthday.setText(document.get("birthday").toString());
                        editDescription.setText(document.get("description").toString());
                        pic = document.get("picture").toString();
                    }
                }
            }
        });

        editBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditProfile.this, d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = editName.getText().toString();
                String statusText = editStatus.getText().toString();
                String birthdayText = editBirthday.getText().toString();
                String descriptionText = editDescription.getText().toString();

                Map<String, Object> profileToSave = new HashMap<String, Object>();
                profileToSave.put(NAME_KEY, nameText);
                profileToSave.put(STATUS_KEY, statusText);
                profileToSave.put(BIRTHDAY_KEY, birthdayText);
                profileToSave.put(DESCRIPTION_KEY, descriptionText);
                profileToSave.put(PICTURE_KEY, pic);

                db.collection("Profiles").document(profile)
                        .update(profileToSave)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "Profile successfully updated",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditProfile.this, ProfilePage.class);
                                intent.putExtra("PROFILE", profile);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfile.this, "Profile update failed",
                                        Toast.LENGTH_LONG).show();
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