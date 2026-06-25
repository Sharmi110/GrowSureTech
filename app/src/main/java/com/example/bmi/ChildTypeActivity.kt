package com.example.bmi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChildTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_type)

        val btnExisting = findViewById<Button>(R.id.btnExisting)
        val btnNew = findViewById<Button>(R.id.btnNew)

        btnExisting.setOnClickListener {

            Toast.makeText(
                this,
                "Opening Capture Page",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(
                Intent(
                    this,
                    CaptureActivity::class.java
                )
            )
        }

        btnNew.setOnClickListener {

            Toast.makeText(
                this,
                "Opening Registration Page",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
    }
}