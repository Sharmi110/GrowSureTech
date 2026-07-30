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

            // Retrieve the worker's region
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val workerRegion = sharedPref.getString("workerRegion", "Trichy")

            Toast.makeText(this, "Generating ID and Saving...", Toast.LENGTH_SHORT).show()

            // ---------------------------------------------------------
            // 🌐 NETWORK CHECK: ROUTE BASED ON CONNECTION
            // ---------------------------------------------------------
            if (NetworkUtils.isOnline(this)) {

                // --- ONLINE MODE ---
                // Wait for Firebase to read the counter and generate GSTC00X
                val counterRef = db.collection("counters").document("childCounter")

                counterRef.get().addOnSuccessListener { counterDoc ->
                    var lastNumber = 0L
                    if (counterDoc.exists()) {
                        lastNumber = counterDoc.getLong("lastNumber") ?: 0L
                    }

                    lastNumber++
                    val childId = String.format("GSTC%03d", lastNumber)

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

                    db.collection("children").document(childId).set(child)
                        .addOnSuccessListener {
                            counterRef.update("lastNumber", lastNumber).addOnSuccessListener {
                                Toast.makeText(this, "Success! Child ID: $childId", Toast.LENGTH_LONG).show()

                                val intent = Intent(this@RegisterActivity, CaptureActivity::class.java)
                                intent.putExtra("CHILD_ID", childId)
                                intent.putExtra("CHILD_DOB", dob)
                                intent.putExtra("CHILD_GENDER", gender)
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

            } else {

                // --- OFFLINE MODE ---
                // Skip the counter read! Generate a short 4-digit PIN so it's easy to remember.
                val randomPin = (1000..9999).random()
                val offlineId = "GSTC_$randomPin"

                val child = hashMapOf(
                    "childId" to offlineId,
                    "childName" to childName,
                    "dob" to dob,
                    "gender" to gender,
                    "parentName" to parentName,
                    "parentPhone" to parentPhoneNumber,
                    "village" to village,
                    "anganwadiCenter" to center,
                    "region" to workerRegion
                )

                // 1. Give data to Firebase (it will safely cache it in the phone's memory automatically)
                db.collection("children").document(offlineId).set(child)

                // 2. MOVE IMMEDIATELY! Do not wait for server listeners to trigger!
                Toast.makeText(this, "Saved Offline! Child ID: $offlineId", Toast.LENGTH_LONG).show()

                val intent = Intent(this@RegisterActivity, CaptureActivity::class.java)
                intent.putExtra("CHILD_ID", offlineId)
                intent.putExtra("CHILD_DOB", dob)
                intent.putExtra("CHILD_GENDER", gender)
                startActivity(intent)
                finish()
            }
        }
    }
}