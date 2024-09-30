package com.example.mobilt_java23_alexandra_nimhagen_lifecycle_v4

import android.annotation.SuppressLint
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

    // Declare Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Get references to the UI elements
        val logIn = findViewById<Button>(R.id.logInButton) // Login button
        val emailEditText = findViewById<EditText>(R.id.editTextEmailAddress) // Email input field
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword) // Password input field
        val registerClick = findViewById<Button>(R.id.registerButton) // Register button

        // Set up a click listener for the login button
        logIn.setOnClickListener {
            // Get email and password from input fields
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Check if fields are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                userLogIn(email, password) // Call function to log in the user
            } else {
                // Show a toast message if fields are empty
                Toast.makeText(this, "Fields can not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up a click listener for the register button
        registerClick.setOnClickListener {
            // Start RegisterActivity when the register button is clicked
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Adjust the padding of the main view to avoid overlaps with system UI elements
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Set padding based on system bars
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Return insets
        }
    }

    // Function to handle user login
    private fun userLogIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Show a welcome message with the user's email
                    Toast.makeText(this, "Welcome ${user?.email}", Toast.LENGTH_LONG).show()
                    // Start the ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    finish() // Close MainActivity
                } else {
                    // Show an error message if authentication failed
                    Toast.makeText(
                        this,
                        "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
            // Log an error message if login fails
            .addOnFailureListener { e ->
                Log.d("Alex", "Log in not possible:", e)
            }
    }
}
