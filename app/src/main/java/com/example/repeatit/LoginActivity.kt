package com.example.repeatit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var userRepository: UserRepository
    private lateinit var signUpTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)  // Pastikan ini sesuai dengan layout Anda

        // Inisialisasi objek UserRepository
        userRepository = UserRepository(this)

        // Menyambungkan UI dengan variabel
        usernameEditText = findViewById(R.id.usernameText)
        passwordEditText = findViewById(R.id.passwordText)
        signInButton = findViewById(R.id.signInButton)
        signUpTextView = findViewById(R.id.signUpTextView)

        signUpTextView.setOnClickListener {
            // Intent untuk berpindah ke SignUpActivity
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Menambahkan aksi ketika tombol login ditekan
        signInButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Verifikasi login
            val isValid = userRepository.verifyLogin(username, password)
            if (isValid) {
                if(username == "admin"){
                val intent = Intent(this@LoginActivity, AdminAddSongActivity::class.java)
                startActivity(intent)
                finish()
                }else{
                val intent = Intent(this@LoginActivity, RepeatItMainActivity::class.java)
                startActivity(intent)
                finish() }
            } else {
                // Login gagal
                Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
