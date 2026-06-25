package com.example.bmi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ParentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        val etStudentName = findViewById<EditText>(R.id.etStudentName)
        val etStudentId = findViewById<EditText>(R.id.etStudentId)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        btnSearch.setOnClickListener {

            val studentName = etStudentName.text.toString().trim()
            val studentId = etStudentId.text.toString().trim()

            if (studentName.isEmpty() || studentId.isEmpty()) {

                Toast.makeText(
                    this,
                    "Enter Student Name and Student ID",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Details Submitted Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // Future:
                // DashboardActivity open pannalaam
            }
        }
    }
}