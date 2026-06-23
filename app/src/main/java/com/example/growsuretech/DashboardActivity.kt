package com.example.growsuretech // IMPORTANT: Keep your exact package name here!

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class DashboardActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var speechText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the default Action Bar and tint the Status Bar
        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#0A58CA")

        setContentView(R.layout.activity_dashboard)

        // Initialize TextToSpeech Engine
        tts = TextToSpeech(this, this)

        // Link logic to the XML layout components
        val tvHeight = findViewById<TextView>(R.id.tvParentHeight)
        val tvWeight = findViewById<TextView>(R.id.tvParentWeight)
        val tvBmi = findViewById<TextView>(R.id.tvParentBMI)
        val tvStatusTitle = findViewById<TextView>(R.id.tvStatusTitle)
        val tvStatusDesc = findViewById<TextView>(R.id.tvStatusDesc)
        val cardStatus = findViewById<CardView>(R.id.cardStatusBackground)
        val btnViewAnalytics = findViewById<Button>(R.id.btnViewAnalytics)
        val btnVoiceAlert = findViewById<Button>(R.id.btnVoiceAlert)

        val tvBadgeStatusIcon = findViewById<TextView>(R.id.tvBadgeStatusIcon)
        val tvBadgeStatusText = findViewById<TextView>(R.id.tvBadgeStatusText)

        // Setup Parameter Variables
        val currentWeightKg = 28.0
        val currentHeightCm = 125.0

        // Execute the WHO Classification Math Engine
        val healthResult = HealthCalculator.calculateHealthStatus(currentWeightKg, currentHeightCm)

        // Render Calculated Metrics
        tvHeight.text = "${currentHeightCm} cm"
        tvWeight.text = "${currentWeightKg} kg"
        tvBmi.text = "${healthResult.bmi}"

        // Execute Reactive UI Updates Based on Classification
        when (healthResult.status) {
            "Normal" -> {
                tvStatusTitle.text = "Overall Health Status: Good"
                tvStatusTitle.setTextColor(Color.parseColor("#2E7D32"))
                speechText = "Great job! Your child is growing well. Keep maintaining a balanced diet."
                tvStatusDesc.text = speechText
                tvStatusDesc.setTextColor(Color.parseColor("#1B5E20"))
                cardStatus.setCardBackgroundColor(Color.parseColor("#E8F5E9"))

                tvBadgeStatusIcon.text = "🏆"
                tvBadgeStatusText.text = "Ideal Growth"
            }
            "Underweight", "Severely Underweight" -> {
                tvStatusTitle.text = "Overall Health Status: ${healthResult.status}"
                tvStatusTitle.setTextColor(Color.parseColor("#C62828"))
                speechText = "Attention needed. Your child's classification tracking shows ${healthResult.status}. Please consult with an Anganwadi worker for a nutrition plan."
                tvStatusDesc.text = speechText
                tvStatusDesc.setTextColor(Color.parseColor("#B71C1C"))
                cardStatus.setCardBackgroundColor(Color.parseColor("#FFEBEE"))

                tvBadgeStatusIcon.text = "💪"
                tvBadgeStatusText.text = "Growing Stronger"
            }
            "Overweight", "Obese" -> {
                tvStatusTitle.text = "Overall Health Status: ${healthResult.status}"
                tvStatusTitle.setTextColor(Color.parseColor("#EF6C00"))
                speechText = "Attention needed. Your child's classification tracking shows ${healthResult.status}. Monitor diet, encourage more physical activity, and reduce sugar intake."
                tvStatusDesc.text = speechText
                tvStatusDesc.setTextColor(Color.parseColor("#E65100"))
                cardStatus.setCardBackgroundColor(Color.parseColor("#FFF3E0"))

                tvBadgeStatusIcon.text = "🏃"
                tvBadgeStatusText.text = "Active Tracker"
            }
        }

        // Button Click Event Listeners
        btnVoiceAlert.setOnClickListener {
            tts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "")
            Toast.makeText(this, "Playing Audio...", Toast.LENGTH_SHORT).show()
        }

        // ROUTES TO THE NEW ANALYTICS SCREEN
        btnViewAnalytics.setOnClickListener {
            val intent = Intent(this, ParentAnalyticsActivity::class.java)
            startActivity(intent)
        }

        // Configure Bottom Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_dashboard

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Navigating to Home...", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_capture -> {
                    Toast.makeText(this, "Opening Camera...", Toast.LENGTH_SHORT).show()
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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.ENGLISH
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}