package com.example.pigeon_mach3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private boolean hasExistingMessage = false;
    private String existingMessage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        mDatabase = FirebaseDatabase.getInstance().getReference("messages");

        Button readButton = findViewById(R.id.readButton);
        Button writeButton = findViewById(R.id.writeButton);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readMessage();
            }
        });
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMessage();
            }
        });
    }
    private void readMessage() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String message = dataSnapshot.getValue(String.class);
                if (message != null) {
                    Toast.makeText(FirebaseActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FirebaseActivity.this, "No message found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FirebaseActivity.this, "Error reading message", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void writeMessage() {
        EditText messageEditText = new EditText(this);

        // Check if there is an existing message
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                existingMessage = dataSnapshot.getValue(String.class);
                if (existingMessage != null) {
                    hasExistingMessage = true;
                    new AlertDialog.Builder(FirebaseActivity.this)
                            .setTitle("Existing Message")
                            .setMessage(existingMessage)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showWriteDialog(messageEditText);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    showWriteDialog(messageEditText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FirebaseActivity.this, "Error checking for existing message", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showWriteDialog(EditText messageEditText) {
        new AlertDialog.Builder(this)
                .setTitle("Write message")
                .setView(messageEditText)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = messageEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(message)) {
                            if (hasExistingMessage) {
                                new AlertDialog.Builder(FirebaseActivity.this)
                                        .setTitle("Existing Message")
                                        .setMessage(existingMessage)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mDatabase.setValue(message);
                                                Toast.makeText(FirebaseActivity.this, "Message written successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            } else {
                                mDatabase.setValue(message);
                                Toast.makeText(FirebaseActivity.this, "Message written successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(FirebaseActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    } }





