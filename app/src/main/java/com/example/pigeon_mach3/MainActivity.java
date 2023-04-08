package com.example.pigeon_mach3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button oldPigeonBtn, newPigeonBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newPigeonBtn = findViewById(R.id.newPigeonBtn);
        oldPigeonBtn = findViewById(R.id.oldPigeonBtn);

        newPigeonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewPigeonActivity.class);
                startActivity(intent);
            }
        });

        oldPigeonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OldPigeonActivity.class);
                startActivity(intent);
            }
        });
    }
}
