package com.example.growsuretech

import android.content.Context
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
        val etParentPhoneNumber = findViewById<EditText>(R.id.etParentPhoneNumber)
        val etVillage = findViewById<EditText>(R.id.etVillage)
        val etCenter = findViewById<EditText>(R.id.etCenter)

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val childName = etChildName.text.toString().trim()
            val dob = etDob.text.toString().trim()
            val gender = etGender.text.toString().trim()
            val parentName = etParentName.text.toString().trim()
            val parentPhoneNumber = etParentPhoneNumber.text.toString().trim()
            val village = etVillage.text.toString().trim()
            val center = etCenter.text.toString().trim()

            if (
                childName.isEmpty() || dob.isEmpty() || gender.isEmpty() ||
                parentName.isEmpty() || parentPhoneNumber.isEmpty() ||
                village.isEmpty() || center.isEmpty()
            ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Generating ID and Saving...", Toast.LENGTH_SHORT).show()

            // 1. Read the counter to get the next ID number
            val counterRef = db.collection("counters").document("childCounter")

            counterRef.get().addOnSuccessListener { counterDoc ->
                var lastNumber = 0L
                if (counterDoc.exists()) {
                    lastNumber = counterDoc.getLong("lastNumber") ?: 0L
                }

                // Increment for the new child
                lastNumber++

                // Format the ID to look like GSTC001, GSTC002, etc.
                val childId = String.format("GSTC%03d", lastNumber)

                // Retrieve the worker's region from when they logged in
                val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                val workerRegion = sharedPref.getString("workerRegion", "Trichy")

                // 2. Create the Child Object exactly how backend requested
                val child = hashMapOf(
                    "childId" to childId,
                    "childName" to childName,
                    "dob" to dob,
                    "gender" to gender,
                    "parentName" to parentName,
                    "parentPhone" to parentPhoneNumber,
                    "village" to village,
                    "anganwadiCenter" to center,
                    "region" to workerRegion
                )

                // 3. Save the child to the "children" collection using their new ID
                db.collection("children").document(childId).set(child)
                    .addOnSuccessListener {

                        // 4. Update the counter so the next child gets the next number
                        counterRef.update("lastNumber", lastNumber).addOnSuccessListener {

                            // 5. Success! Open the Camera
                            Toast.makeText(this, "Success! Child ID: $childId", Toast.LENGTH_LONG).show()
                            // Pack the ID and DOB into the intent before opening the camera
                            val intent = Intent(this@RegisterActivity, CaptureActivity::class.java)
                            intent.putExtra("CHILD_ID", childId)
                            intent.putExtra("CHILD_DOB", dob)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save child: ${e.message}", Toast.LENGTH_LONG).show()
                    }

            }.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to connect to counter: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}