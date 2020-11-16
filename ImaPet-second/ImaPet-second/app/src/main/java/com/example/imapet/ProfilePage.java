package com.example.imapet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfilePage extends AppCompatActivity {
    TextView profileName, status, description, birthday;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profileName = (TextView) findViewById(R.id.txtProfileName);
        status = (TextView) findViewById(R.id.txtStatus);
        description = (TextView) findViewById(R.id.txtDescription);
        birthday = (TextView) findViewById(R.id.txtBirthday);

        String profile = getIntent().getStringExtra("PROFILE");
        profileName.setText(profile);

        db.collection("Profiles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("name").equals(profile)){
                                    status.setText("Status: " + document.get("status").toString());
                                    birthday.setText("Birthday: " + document.get("birthday").toString());
                                    description.setText("Description: " + document.get("description").toString());
                                }
                            }
                        } else {
                            Toast.makeText(ProfilePage.this, "Error loading profile data",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Button logoutButton = (Button)findViewById(R.id.logoutButton);
        Button communityButton = (Button)findViewById(R.id.communityButton);
        ImageButton settings = (ImageButton) findViewById(R.id.imgBtn_settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilePage.this, SettingsActivity.class));
            }
        });

        FloatingActionButton newPost = (FloatingActionButton)findViewById(R.id.newPost);
        FloatingActionButton editProfile = (FloatingActionButton) findViewById(R.id.editProfile);

        //add cursor for retrieving information for a single profile



        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this, CreatePost.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this, Login.class));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this, Profile.class));
            }
        });

        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, PostListActivity.class);
                intent.putExtra("PROFILE", profile);
                startActivity(intent);
            }
        });

        androidx.preference.PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_LOCATION_SWITCH, false);
        Toast.makeText(this, switchPref.toString(), Toast.LENGTH_SHORT).show();

    }
}