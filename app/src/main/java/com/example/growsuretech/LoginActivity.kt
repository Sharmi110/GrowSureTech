package com.example.growsuretech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = FirebaseFirestore.getInstance()

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Checking database...", Toast.LENGTH_SHORT).show()

            db.collection("users").document(username).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {

                        // FIX: Now we actually check the password!
                        val storedPassword = document.getString("password")
                        val role = document.getString("role")

                        if (storedPassword != password) {
                            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener // Stops the login process right here
                        }

                        // PRO IMPROVEMENT: Save the worker's region temporarily on the phone
                        val region = document.getString("region") ?: "Trichy"
                        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("workerRegion", region).apply()

                        if (role == "worker") {
                            Toast.makeText(this, "Welcome Anganwadi Worker!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, ChildTypeActivity::class.java))
                            finish()
                        }
                        else if (role == "districtOfficer") {
                            Toast.makeText(this, "Welcome District Officer!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, AdminDashboardActivity::class.java))
                            finish()
                        }

                    } else {
                        // Parent Login (No changes needed here)
                        db.collection("children").document(username).get()
                            .addOnSuccessListener { childDoc ->
                                if (childDoc != null && childDoc.exists()) {
                                    val dob = childDoc.getString("dob")

                                    if (dob == password) {
                                        Toast.makeText(this, "Welcome Parent!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, DashboardActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Incorrect Password (DOB)", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Database Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}