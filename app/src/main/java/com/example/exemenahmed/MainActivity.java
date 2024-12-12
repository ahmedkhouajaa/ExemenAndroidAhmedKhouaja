package com.example.exemenahmed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button signInButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        // Navigate to SignInActivity when Sign In button is clicked
        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SiginIn.class);
            startActivity(intent);
        });

        // Navigate to SignUpActivity when Sign Up button is clicked
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SiginUp.class);
            startActivity(intent);
        });
    }
}
