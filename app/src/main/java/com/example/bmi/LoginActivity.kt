package com.example.bmi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val db = FirebaseFirestore.getInstance()

            val data = hashMapOf(
                "name" to "Praveen",
                "time" to System.currentTimeMillis()
            )

            db.collection("zzz_praveen_test_2026")
                .add(data)
                .addOnSuccessListener { documentReference ->

                    Toast.makeText(
                        this,
                        "Login Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(
                            this,
                            RoleActivity::class.java
                        )
                    )
                }
                .addOnFailureListener { e ->

                    Toast.makeText(
                        this,
                        "Firebase Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}