package com.example.mobilt_java23_lovisa_hjalmar_lifecycle_v4

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ProfileActivity : AppCompatActivity() {

    var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    lateinit var license:String

    lateinit var profileName: EditText
    lateinit var profileEmail: EditText
    lateinit var profilePhone: EditText
    lateinit var profileDate: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        val menuBtn = findViewById<Button>(R.id.menuButton)
        val profileName = findViewById<EditText>(R.id.nameProfileText)
        val profileEmail = findViewById<EditText>(R.id.emailProfileText)
        val profilePhone = findViewById<EditText>(R.id.phoneProfileText)
        val profileDate = findViewById<EditText>(R.id.dateProfileText)
        val profileLicense = findViewById<CheckBox>(R.id.licenseProfileCheck)
        val profileTerms = findViewById<RadioButton>(R.id.termsAndConditionsRadio)
        val profileSave = findViewById<Button>(R.id.saveFormBtn)

        displayUserInfo { user ->
            profileName.setText(user["name"] as String?)
            profileEmail.setText(user["email"] as String?)
            profilePhone.setText(user["phone"] as String?)
            profileDate.setText(user["birthday"] as String?)
        }


        menuBtn.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        //displayUserInfo()


        if (profileLicense.isChecked){
            license = "yes"
        } else {
            license = "no"
        }

        profileSave.setOnClickListener {
            if (profileTerms.isActivated) {
                Log.d("Lovisa", "profile saved")
                update()
            } else Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun displayUserInfo(callback: (user: Map<String, Any>) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        callback(document.data ?: emptyMap())
                    } else {
                        Log.d("Lovisa", "finns ej")
                    }
                }
        }

    }


    fun update() {
        val user = hashMapOf(
            "name" to profileName.text.toString(),
            "email" to profileEmail.text.toString(),
            "phone" to profilePhone.text.toString(),
            "birthday" to profileDate.text.toString(),
            "license" to license,
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

