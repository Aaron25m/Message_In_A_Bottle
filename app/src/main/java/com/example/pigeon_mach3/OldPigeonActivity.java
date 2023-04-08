package com.example.pigeon_mach3;
import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OldPigeonActivity extends AppCompatActivity {
    private EditText nameEditText, passwordEditText;
    private Button loginBtn;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pigeon);

        nameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginBtn);

        // Initialize the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(OldPigeonActivity.this, "Please enter both name and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the user exists in the database
                    Query query = usersRef.orderByChild("name").equalTo(name);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean foundUser = false;

                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);

                                if (user.password.equals(password)) {
                                    foundUser = true;
                                    user.userId = userSnapshot.getKey(); // set the user's ID
                                    // Print a welcome toast and redirect the user to the FirebaseActivity
                                    Toast.makeText(OldPigeonActivity.this, "Welcome " + user.name + "!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(OldPigeonActivity.this, FirebaseActivity.class);
                                    intent.putExtra("userId", user.userId); // add the user's ID to the intent
                                    startActivity(intent);
                                }
                            }

                            if (!foundUser) {
                                Toast.makeText(OldPigeonActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(OldPigeonActivity.this, "Error accessing Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    // Define the User class with name, password, userId, and messagesRef properties
    public static class User {
        public String name;
        public String password;
        public String userId;
        public List<String> messages; // add a list of messages

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name, String password) {
            this.name = name;
            this.password = password;
            messages = new ArrayList<>(); // initialize the list of messages
        }
    }

}
