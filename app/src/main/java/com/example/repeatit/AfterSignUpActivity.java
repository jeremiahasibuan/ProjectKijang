package com.example.repeatit;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AfterSignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_signup_activity);

        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(AfterSignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}

