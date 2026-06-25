package com.example.bmi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = FirebaseFirestore.getInstance()

        val etChildName = findViewById<EditText>(R.id.etChildName)
        val etDob = findViewById<EditText>(R.id.etDob)
        val etGender = findViewById<EditText>(R.id.etGender)
        val etParentName = findViewById<EditText>(R.id.etParentName)
        val etParentPhoneNumber =
            findViewById<EditText>(R.id.etParentPhoneNumber)
        val etVillage = findViewById<EditText>(R.id.etVillage)
        val etCenter = findViewById<EditText>(R.id.etCenter)

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val childName = etChildName.text.toString().trim()
            val dob = etDob.text.toString().trim()
            val gender = etGender.text.toString().trim()
            val parentName = etParentName.text.toString().trim()
            val parentPhoneNumber =
                etParentPhoneNumber.text.toString().trim()
            val village = etVillage.text.toString().trim()
            val center = etCenter.text.toString().trim()

            if (
                childName.isEmpty() ||
                dob.isEmpty() ||
                gender.isEmpty() ||
                parentName.isEmpty() ||
                parentPhoneNumber.isEmpty() ||
                village.isEmpty() ||
                center.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val user = hashMapOf(
                "childName" to childName,
                "dob" to dob,
                "gender" to gender,
                "parentName" to parentName,
                "parentPhoneNumber" to parentPhoneNumber,
                "village" to village,
                "anganwadiCenter" to center
            )

            db.collection("students")
                .add(user)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Registered Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(
                            this,
                            CaptureActivity::class.java
                        )
                    )

                    finish()
                }
                .addOnFailureListener { e ->

                    Toast.makeText(
                        this,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}