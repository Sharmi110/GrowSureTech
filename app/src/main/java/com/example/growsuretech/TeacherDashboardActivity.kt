package com.example.growsuretech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TeacherDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This links to your Anganwadi Worker XML layout
        setContentView(R.layout.activity_teacher_dashboard)

        // Connect the buttons from your XML
        val btnViewRahul = findViewById<Button>(R.id.btnViewRahul)
        val btnViewPriya = findViewById<Button>(R.id.btnViewPriya)
        val btnRecordNew = findViewById<Button>(R.id.btnRecordNew)

        // 1. OPEN RAHUL'S DASHBOARD
        btnViewRahul.setOnClickListener {
            // This navigates to the DashboardActivity code you just pasted earlier!
            val intent = Intent(this, DashboardActivity::class.java)

            // ⚠️ Change "GSTC001" to an actual ID that exists in your Firebase database
            intent.putExtra("CHILD_ID", "GSTC001")
            startActivity(intent)
        }

        // 2. OPEN PRIYA'S DASHBOARD
        btnViewPriya.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)

            // ⚠️ Change "GSTC002" to an actual ID that exists in your Firebase database
            intent.putExtra("CHILD_ID", "GSTC002")
            startActivity(intent)
        }

        // 3. OPEN THE REGISTRATION/CAMERA FLOW
        btnRecordNew.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}