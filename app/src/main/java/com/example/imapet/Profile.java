package com.example.imapet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class Profile extends AppCompatActivity {
    ProfileDatabase profileDB;
    EditText userName, profileName, status, description, birthday;
    Button btnCreateProfile, btnViewProfile, btnUpdateProfile, btnDeleteProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileDB = new ProfileDatabase(this);

        userName = (EditText) findViewById(R.id.editText_username);
        profileName = (EditText) findViewById(R.id.editText_profileName);
        status = (EditText) findViewById(R.id.editText_status);
        description = (EditText) findViewById(R.id.editText_description);
        birthday = (EditText) findViewById(R.id.editText_birthday);

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Profile.this, d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnCreateProfile = (Button) findViewById(R.id.btn_create);
        createProfile();

        btnViewProfile = (Button) findViewById(R.id.btn_view);
        viewProfile();

        btnUpdateProfile = (Button) findViewById(R.id.btn_update);
        editProfile();

        btnDeleteProfile = (Button) findViewById(R.id.btn_delete);
        deleteProfile();
    }

    public void createProfile() {
        btnCreateProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ((userName.getText().toString()).isEmpty() || (userName.getText().toString().matches("^\\s*$"))) {
                            Toast.makeText(Profile.this, "Error: cannot create a profile without a username", Toast.LENGTH_LONG).show();
                        } else {
                            boolean isCreated = profileDB.insertProfileData(userName.getText().toString(), profileName.getText().toString(), status.getText().toString(), description.getText().toString(), birthday.getText().toString());

                            if (isCreated == true) {
                                Toast.makeText(Profile.this, "Profile Created", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Profile.this, ProfilePage.class));
                            } else {
                                Toast.makeText(Profile.this, "Error: failed to create profile!", Toast.LENGTH_LONG).show();
                            }
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
                            sb.append("Birthday: " + retrieval.getString(4) + "\n\n");
                        }

                        displayMessage("Profile Data", sb.toString());
                    /**  Cursor retrieval2 = profileDB.getSingleProfile(userName.getText().toString());

                        if (retrieval2.getCount() == 0) {
                            displayMessage("ERROR", "Data does not exist or has not been found");
                            return;
                        }

                        StringBuffer sb = new StringBuffer();

                        while (retrieval2.moveToNext()) {
                            sb.append("Username: " + retrieval2.getString(0) + "\n");
                            sb.append("Profile Name: " + retrieval2.getString(1) + "\n");
                            sb.append("Status: " + retrieval2.getString(2) + "\n");
                            sb.append("Description: " + retrieval2.getString(3) + "\n\n");
                        }

                        displayMessage("Profile Data", sb.toString());**/
                    }
                }
        );
    }

    public void editProfile() {
        btnUpdateProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean hasBeenEdited = profileDB.updateProfile(userName.getText().toString(), profileName.getText().toString(), status.getText().toString(), description.getText().toString(), birthday.getText().toString());

                        if (hasBeenEdited == true) {
                            Toast.makeText(Profile.this, "Profile has been updated if an account with that username exists", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Profile.this, "Error: failed to update profile!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
    }

    public void deleteProfile() {
        btnDeleteProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer deleteTableRow = profileDB.deleteProfileData(userName.getText().toString());

                        if (deleteTableRow > 0) {
                            Toast.makeText(Profile.this, "Profile has been deleted if an account with that username had existed", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Profile.this, "Error: failed to delete profile!", Toast.LENGTH_LONG).show();
                        }

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