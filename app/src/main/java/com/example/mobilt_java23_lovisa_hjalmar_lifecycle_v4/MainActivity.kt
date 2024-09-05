package com.example.mobilt_java23_lovisa_hjalmar_lifecycle_v4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val logIn = findViewById<Button>(R.id.logInButton)
        val emailEditText = findViewById<EditText>(R.id.editTextEmailAddress)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val registerClick = findViewById<Button>(R.id.registerButton)


        logIn.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                userLogIn(email, password)
            } else {
                Toast.makeText(this, "Fields can not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        registerClick.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun userLogIn (email: String, password: String) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Welcome ${user?.email}", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("Lovisa", "Log in not possible:", e)
                }
        }
    }