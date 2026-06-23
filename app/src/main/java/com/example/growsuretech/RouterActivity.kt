package com.example.growsuretech

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#0A58CA")
        setContentView(R.layout.activity_router)

        val btnParent = findViewById<Button>(R.id.btnRouteParent)
        val btnTeacher = findViewById<Button>(R.id.btnRouteTeacher)
        val btnAdmin = findViewById<Button>(R.id.btnRouteAdmin)

        // Route to Parent View
        btnParent.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        // Route to Teacher View
        btnTeacher.setOnClickListener {
            startActivity(Intent(this, TeacherDashboardActivity::class.java))
        }

        // Route to Admin View
        btnAdmin.setOnClickListener {
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }
    }
}