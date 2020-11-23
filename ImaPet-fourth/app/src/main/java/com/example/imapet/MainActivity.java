package com.example.imapet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String EMAIL_KEY = "email";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String ADMIN_KEY = "admin";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registerButton = (Button)findViewById(R.id.registerButton);
        Button loginPageButton = (Button)findViewById(R.id.loginPageButton);

        loginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        /*
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    EditText username = (EditText) findViewById(R.id.username);
                                    String usernameText = username.getText().toString();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("username").equals(usernameText)){
                                            db.collection("Users").document(document.getId())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(MainActivity.this, "Account successfully deleted",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MainActivity.this, "Account deletion failed",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }
                                    }
                                } else {

                                }
                            }
                        });
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    EditText username = (EditText) findViewById(R.id.username);
                                    String usernameText = username.getText().toString();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("username").equals(usernameText)){
                                            EditText email = (EditText) findViewById(R.id.email);
                                            EditText password = (EditText) findViewById(R.id.password);
                                            String emailText = email.getText().toString();
                                            String passwordText = password.getText().toString();

                                            if(emailText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty()  || emailText.matches("^\\s*$") || usernameText.matches("^\\s*$") || passwordText.matches("^\\s*$")) { return; }
                                            Map<String, Object> accountToSave = new HashMap<String, Object>();
                                            accountToSave.put(EMAIL_KEY, emailText);
                                            accountToSave.put(PASSWORD_KEY, passwordText);
                                            db.collection("Users").document(document.getId())
                                                    .update(accountToSave)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(MainActivity.this, "Account successfully updated",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MainActivity.this, "Account update failed",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }
                                    }
                                } else {

                                }
                            }
                        });
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView accountList = (TextView) findViewById(R.id.accountList);
                accountList.setText("");
                StringBuilder s = new StringBuilder(1000);
                db.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int count = 1;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        s.append("Account " + count);
                                        s.append(document.getData().toString());
                                        s.append("\n\n");
                                        count++;
                                    }
                                    accountList.setText(s);
                                } else {

                                }
                            }
                        });
            }
        });

         */

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.email);
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                String emailText = email.getText().toString();
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();

                if(emailText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty()  || emailText.matches("^\\s*$") || usernameText.matches("^\\s*$") || passwordText.matches("^\\s*$")) { return; }
                Map<String, Object> accountToSave = new HashMap<String, Object>();
                accountToSave.put(EMAIL_KEY, emailText);
                accountToSave.put(USERNAME_KEY, usernameText);
                accountToSave.put(PASSWORD_KEY, passwordText);
                accountToSave.put(ADMIN_KEY, false);

                db.collection("Users")
                        .add(accountToSave)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MainActivity.this, "Account created successfully",
                                        Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, Profile2.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error saving account information",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}