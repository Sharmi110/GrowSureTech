package com.example.bmi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ExistingChildActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_child)

        Toast.makeText(
            this,
            "Existing Child Opened",
            Toast.LENGTH_LONG
        ).show()
    }
}