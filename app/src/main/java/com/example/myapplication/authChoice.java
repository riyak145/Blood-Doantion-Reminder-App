package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class authChoice extends AppCompatActivity {

    Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_choice);

        loginButton = findViewById(R.id.Login);
        signupButton = findViewById(R.id.Signup);

        Log.d("AuthChoice", "Login Button: " + loginButton);
        Log.d("AuthChoice", "Signup Button: " + signupButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to start ThirdActivity
                Intent intent = new Intent(authChoice.this, Login.class);
                // Start the M
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(authChoice.this, sign_up.class);
                startActivity(intent);
            }
        });
    }
}
