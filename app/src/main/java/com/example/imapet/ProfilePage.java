package com.example.imapet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfilePage extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 9;
    private static final int CAMERA_REQUEST = 11;

    TextView profileName, status, description, birthday;
    ImageView profilePic;
    Context context = this;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profilePic = (ImageView) findViewById(R.id.iv_profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayOptions();
            }
        });

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
    }

   //Methods for the camera
    private void displayOptions(){
        final String[] chooseOption = getResources().getStringArray(R.array.options_for_image);

        AlertDialog.Builder construct = new AlertDialog.Builder(context);
        construct.setTitle("Image Options").setItems(chooseOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        useGallery();
                        break;
                    case 1:
                        useCamera();
                        break;
                }
            }
        });
        AlertDialog message = construct.create();
        message.show();
    }

    private void useCamera(){
        Intent camera = new Intent();
        camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST);
    }

    private void useGallery(){
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri chosenPicture = data.getData();
            profilePic.setImageURI(chosenPicture);

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            profilePic.setImageBitmap(bitmap);
        }
    }
}