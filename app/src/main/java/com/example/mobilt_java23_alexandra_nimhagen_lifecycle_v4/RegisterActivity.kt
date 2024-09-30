package com.example.mobilt_java23_alexandra_nimhagen_lifecycle_v4

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    // Lazy initialization of Firestore
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // Late initialization for EditText components
    lateinit var registerPassword: EditText
    lateinit var registerEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Set the layout for this activity

        // Initialize UI elements
        val backBtn = findViewById<Button>(R.id.backButton)
        registerEmail = findViewById(R.id.editTextTextEmailAddress2) // Email input
        registerPassword = findViewById(R.id.editTextTextPassword) // Password input
        val registerCheckAge = findViewById<CheckBox>(R.id.checkBox2) // Age confirmation checkbox
        val addUserBtn = findViewById<Button>(R.id.saveUserButton) // Button to save user

        // Navigate back to MainActivity when back button is clicked
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Save user data if age checkbox is checked
        addUserBtn.setOnClickListener {
            if (registerCheckAge.isChecked) {
                write() // Call the write() method to save user data
            } else {
                Toast.makeText(this, "You have to be 18 to register", Toast.LENGTH_SHORT).show() // Age restriction message
            }
        }
    }

    // Define the write() method to save user data in Firestore
    fun write() {
        // Create a user map with email and password
        val user = hashMapOf(
            "email" to registerEmail.text.toString(),
            "password" to registerPassword.text.toString()
        )

        // Add the user document to Firestore
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference -> // On success
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}") // Log document ID
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show() // Success message
                val intent = Intent(this, MainActivity::class.java) // Navigate to MainActivity
                startActivity(intent)
                finish() // Finish current activity
            }
            .addOnFailureListener { e -> // On failure
                Log.w(TAG, "Error adding document", e) // Log error
                Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show() // Error message
            }
    }
}
