package com.example.mobilt_java23_alexandra_nimhagen_lifecycle_v4

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    // Lazy initialization of Firestore and Auth instances
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var auth: FirebaseAuth

    private lateinit var license: String // To store license status

    // Variables for UI components
    lateinit var profileName: EditText
    lateinit var profileEmail: EditText
    lateinit var profilePhone: EditText
    lateinit var profileDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements
        profileName = findViewById(R.id.nameProfileText)
        profileEmail = findViewById(R.id.emailProfileText)
        profilePhone = findViewById(R.id.phoneProfileText)
        profileDate = findViewById(R.id.dateProfileText)
        val profileLicense = findViewById<CheckBox>(R.id.licenseProfileCheck)
        val profileTerms = findViewById<RadioButton>(R.id.termsAndConditionsRadio)
        val profileSave = findViewById<Button>(R.id.saveFormBtn)
        val menuBtn = findViewById<Button>(R.id.menuButton)

        // Display user information in the EditTexts
        displayUserInfo { user ->
            profileName.setText(user["name"] as String?)
            profileEmail.setText(user["email"] as String?)
            profilePhone.setText(user["phone"] as String?)
            profileDate.setText(user["birthday"] as String?)
        }

        // Navigate to the menu when the menu button is clicked
        menuBtn.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        // Handle the driver's license checkbox
        license = if (profileLicense.isChecked) {
            "yes"
        } else {
            "no"
        }

        // Save user information when the save button is clicked
        profileSave.setOnClickListener {
            if (profileTerms.isChecked) {
                Log.d("Alex", "Profile saved")
                update()
            } else {
                // Show a message if terms are not accepted
                Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle window insets for UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Get the size of system UI elements (like status bar)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Set padding to avoid overlap with system UI elements
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Return the insets
        }
    }

    // Fetch and display user information from Firestore
    private fun displayUserInfo(callback: (user: Map<String, Any>) -> Unit) {
        val userId = auth.currentUser?.uid // Get the current user's ID
        if (userId != null) {
            db.collection("users").document(userId)
                .get() // Get the document
                .addOnSuccessListener { document ->
                    if (document != null) {
                        callback(document.data ?: emptyMap())
                    } else {
                        Log.d("Alex", "Document does not exist")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error fetching document", e)
                }
        } else {
            Log.d("Alex", "User is not logged in.")
        }
    }

    // Update user information in Firestore
    fun update() {
        val user = hashMapOf(
            "name" to profileName.text.toString(),
            "email" to profileEmail.text.toString(),
            "phone" to profilePhone.text.toString(),
            "birthday" to profileDate.text.toString(),
            "license" to license
        )
        // Add user data to Firestore
        db.collection("users")
            .add(user) // Add the user data
            .addOnSuccessListener { documentReference -> // On success
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e -> // On failure
                Log.w(TAG, "Error adding document", e)
            }
    }
}
