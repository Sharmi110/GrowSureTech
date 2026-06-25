package com.example.bmi

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RoleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role)

        val spRole = findViewById<Spinner>(R.id.spRole)
        val btnProceed = findViewById<Button>(R.id.btnProceed)

        val roles = arrayOf(
            "Anganwadi Workers",
            "Parents",
            "System Admin"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            roles
        )

        spRole.adapter = adapter

        btnProceed.setOnClickListener {

            when (spRole.selectedItem.toString()) {

                "Parents" -> {
                    startActivity(
                        Intent(
                            this,
                            ParentActivity::class.java
                        )
                    )
                }

                "Anganwadi Workers" -> {
                    startActivity(
                        Intent(
                            this,
                            ChildTypeActivity::class.java
                        )
                    )
                }

                "System Admin" -> {

                    Toast.makeText(
                        this,
                        "Dashboard Under Development",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }
}