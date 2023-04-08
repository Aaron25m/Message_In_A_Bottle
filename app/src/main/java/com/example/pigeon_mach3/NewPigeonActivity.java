package com.example.pigeon_mach3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewPigeonActivity extends AppCompatActivity {
    private EditText nameEditText, passwordEditText;
    private Button loginBtn;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_pigeon);

        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginBtn);

        // Initialize the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(NewPigeonActivity.this, "Please enter both name and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the username already exists in the database
                    usersRef.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Display an error message if the username already exists
                                Toast.makeText(NewPigeonActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                            } else {
                                // Create a User object with the name, password and an empty messages map
                                Map<String, Object> messages = new HashMap<>();
                                User user = new User(name, password, messages);

                                // Add the user to the DatabaseReference using push()
                                usersRef.push().setValue(user);

                                Toast.makeText(NewPigeonActivity.this, "User data added to Firebase", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle database error
                        }
                    });
                }
            }
        });
    }

    // Define the User class with name, password, and messages properties
    public static class User {
        public String name;
        public String password;
        public Map<String, Object> messages;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name, String password, Map<String, Object> messages) {
            this.name = name;
            this.password = password;
            this.messages = messages;
        }
    }
}
