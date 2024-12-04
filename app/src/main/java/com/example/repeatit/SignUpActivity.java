package com.example.repeatit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SignUpActivity extends AppCompatActivity {

    private EditText emailText, usernameText, passwordText;
    private Button signUpButton;
    private ImageView arrowImage;
    private UsersDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        emailText = findViewById(R.id.emailText);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        signUpButton = findViewById(R.id.signUpButton);
        arrowImage = findViewById(R.id.arrowImage);
        dbHelper = new UsersDBHelper(this);

        arrowImage.setOnClickListener(view ->{
            finish();
        });

        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = emailText.getText().toString();
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                if(email.isEmpty() || username.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }else{
                    dbHelper.addUser(email, username, password);
                    Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                    emailText.setText("");
                    usernameText.setText("");
                    passwordText.setText("");

                    goToAfterSignUpActivity();
                }
            }
        });
    }

    private void goToAfterSignUpActivity() {
        Intent intent = new Intent(SignUpActivity.this, AfterSignUpActivity.class);
        startActivity(intent);
        finish();
    }
}

