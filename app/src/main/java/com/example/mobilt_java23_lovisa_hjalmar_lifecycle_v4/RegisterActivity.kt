package com.example.mobilt_java23_lovisa_hjalmar_lifecycle_v4

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {

    var db = Firebase.firestore

    val backBtn = findViewById<Button>(R.id.backButton)
    val registerEmail = findViewById<EditText>(R.id.editTextTextEmailAddress2)
    val registerPassword = findViewById<EditText>(R.id.editTextTextPassword)
    val registerCheckAge = findViewById<CheckBox>(R.id.checkBox2)
    val addUserBtn = findViewById<Button>(R.id.saveUserButton)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)


        backBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        addUserBtn.setOnClickListener{
            if (registerCheckAge.isChecked) {
                write()
            } else {
                Toast.makeText(this, "You have to be 18 to register", Toast.LENGTH_SHORT).show()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun write() {
        val user = hashMapOf(
            "email" to registerEmail.text.toString(),
            "password" to registerPassword.text.toString(),
        )
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}