package com.example.imapet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Profile extends AppCompatActivity {
    ProfileDatabase profileDB;
    EditText userName, profileName, status, description;
    Button btnCreateProfile, btnViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileDB = new ProfileDatabase(this);

        userName = (EditText) findViewById(R.id.editText_username);
        profileName = (EditText) findViewById(R.id.editText_profileName);
        status = (EditText) findViewById(R.id.editText_status);
        description = (EditText) findViewById(R.id.editText_description);

        btnCreateProfile = (Button) findViewById(R.id.btn_create);
        createProfile();

        btnViewProfile = (Button) findViewById(R.id.btn_view);
        viewProfile();
    }

    public void createProfile() {
        btnCreateProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isCreated = profileDB.insertData(userName.getText().toString(), profileName.getText().toString(), status.getText().toString(), description.getText().toString());

                        if (isCreated == true) {
                            Toast.makeText(Profile.this, "Profile Created", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Profile.this, "Error: failed to create profile!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void viewProfile() {
        btnViewProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor retrieval = profileDB.getAllProfiles();

                        if (retrieval.getCount() == 0) {
                            displayMessage("ERROR", "Data does not exist or has not been found");
                            return;
                        }

                        StringBuffer sb = new StringBuffer();

                        while (retrieval.moveToNext()) {
                            sb.append("Username: " + retrieval.getString(0) + "\n");
                            sb.append("Profile Name: " + retrieval.getString(1) + "\n");
                            sb.append("Status: " + retrieval.getString(2) + "\n");
                            sb.append("Description: " + retrieval.getString(3) + "\n");
                        }

                        displayMessage("Profile Data", sb.toString());
                    }
                }
        );
    }

    public void displayMessage(String title, String Message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle(title);
        alert.setMessage(Message);
        alert.show();
    }
}