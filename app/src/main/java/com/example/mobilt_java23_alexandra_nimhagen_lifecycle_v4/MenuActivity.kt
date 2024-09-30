package com.example.mobilt_java23_alexandra_nimhagen_lifecycle_v4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu) // Set the layout for this activity

        // Initialize the buttons using findViewById
        val profilePageBtn = findViewById<Button>(R.id.profilePageButton)
        val registerBtn = findViewById<Button>(R.id.registerActivityButton)
        val mainPageBtn = findViewById<Button>(R.id.logInPageButton)

        // Set up click listeners for the buttons
        profilePageBtn.setOnClickListener {
            // Start ProfileActivity when profile button is clicked
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        registerBtn.setOnClickListener {
            // Start RegisterActivity when register button is clicked
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        mainPageBtn.setOnClickListener {
            // Start MainActivity when login button is clicked
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Handle window insets to prevent UI overlap with system UI elements
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Get the size of the system bars
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Set padding for the view to avoid overlap with the system bars
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Return the insets
        }
    }
}
