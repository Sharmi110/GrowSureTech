package com.example.growsuretech

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class TeacherDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#0A58CA")
        setContentView(R.layout.activity_teacher_dashboard)

        val btnRecordMeasurement = findViewById<Button>(R.id.btnRecordMeasurement)
        val btnViewRahul = findViewById<Button>(R.id.btnViewRahul)
        val btnViewPriya = findViewById<Button>(R.id.btnViewPriya)

        // ROUTE 1: View Rahul's Full Profile
        btnViewRahul.setOnClickListener {
            // This seamlessly opens the Parent Dashboard showing Rahul's stats!
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        // ROUTE 2: View Priya's Profile (Left as Toast for now)
        btnViewPriya.setOnClickListener {
            Toast.makeText(this, "Opening Priya's Profile...", Toast.LENGTH_SHORT).show()
        }

        // UPDATED: Now actually opens the Camera screen instead of showing a popup!
        btnRecordMeasurement.setOnClickListener {
            val intent = Intent(this, CaptureActivity::class.java)
            startActivity(intent)
        }

        // Setup Bottom Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationViewTeacher)
        bottomNavigationView.selectedItemId = R.id.nav_dashboard

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // UPDATED: Routes back to Role Selection
                    val intent = Intent(this, RoleActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clears the backstack so you don't pile up screens
                    startActivity(intent)
                    true
                }
                R.id.nav_capture -> {
                    // UPDATED: Opens Camera from the Nav Bar
                    val intent = Intent(this, CaptureActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_dashboard -> true
                R.id.nav_profile -> {
                    Toast.makeText(this, "Opening Profile...", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}