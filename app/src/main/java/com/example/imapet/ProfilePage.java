package com.example.imapet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfilePage extends AppCompatActivity {
    TextView profileName, status, description, birthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profileName = (TextView) findViewById(R.id.txtProfileName);
        status = (TextView) findViewById(R.id.txtStatus);
        description = (TextView) findViewById(R.id.txtDescription);
        birthday = (TextView) findViewById(R.id.txtBirthday);

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
                startActivity(new Intent(ProfilePage.this, PostListActivity.class));
            }
        });

        androidx.preference.PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_LOCATION_SWITCH, false);
        Toast.makeText(this, switchPref.toString(), Toast.LENGTH_SHORT).show();

    }
}