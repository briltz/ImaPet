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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerPageButton = (Button)findViewById(R.id.registerPageButton);
        Button loginButton = (Button)findViewById(R.id.loginButton);

        registerPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText username = (EditText) findViewById(R.id.loginUsername);
                EditText password = (EditText) findViewById(R.id.loginPassword);
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();

                db.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("username").equals(usernameText) && document.get("password").equals(passwordText)){
                                            Toast.makeText(Login.this, "Login successful",
                                                    Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Login.this, Profile2.class));
                                        }
                                    }
                                } else {
                                    Toast.makeText(Login.this, "Unable to reach login server",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}