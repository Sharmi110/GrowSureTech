package com.example.growsuretech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject
import java.util.Locale

class DashboardActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var db: FirebaseFirestore

    // 1. DECLARE UI ELEMENTS
    private lateinit var tvParentHeight: TextView
    private lateinit var tvParentWeight: TextView
    private lateinit var tvParentBMI: TextView
    private lateinit var tvStatusTitle: TextView
    private lateinit var tvStatusDesc: TextView

    private lateinit var btnVoiceAlert: Button
    private lateinit var btnViewAnalytics: Button

    private var currentChildId: String = ""

    // 🚀 NEW: Text-to-Speech Engine
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        db = FirebaseFirestore.getInstance()

        // Initialize the Text-to-Speech engine
        tts = TextToSpeech(this, this)

        // 2. CONNECT TO XML IDs
        tvParentHeight = findViewById(R.id.tvParentHeight)
        tvParentWeight = findViewById(R.id.tvParentWeight)
        tvParentBMI = findViewById(R.id.tvParentBMI)
        tvStatusTitle = findViewById(R.id.tvStatusTitle)
        tvStatusDesc = findViewById(R.id.tvStatusDesc)

        btnVoiceAlert = findViewById(R.id.btnVoiceAlert)
        btnViewAnalytics = findViewById(R.id.btnViewAnalytics)

        // 3. CHECK FOR PASSED DATA
        val passedId = intent.getStringExtra("CHILD_ID")

        if (passedId != null) {
            currentChildId = passedId
            fetchMeasurements(currentChildId)
        } else {
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val loggedInUsername = sharedPref.getString("username", "UNKNOWN_USER")
            Toast.makeText(this, "Welcome, $loggedInUsername", Toast.LENGTH_SHORT).show()
        }

        // 4. MAKE BUTTONS FUNCTIONAL

        // 🚀 THE FIX: Trigger the voice engine when clicked
        btnVoiceAlert.setOnClickListener {
            speakOutHealthStatus()
        }

        btnViewAnalytics.setOnClickListener {
            if (currentChildId.isNotEmpty()) {
                val intent = Intent(this, ParentAnalyticsActivity::class.java)
                intent.putExtra("CHILD_ID", currentChildId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Child ID is missing, cannot load chart.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 5. TEXT-TO-SPEECH SETUP
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set language to US English (or default locale)
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language specified is not supported!")
                Toast.makeText(this, "Voice language not supported on this device.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun speakOutHealthStatus() {
        // Grab the exact text currently showing on the screen
        val titleText = tvStatusTitle.text.toString()
        val descText = tvStatusDesc.text.toString()

        // Combine them into one natural sentence
        val textToRead = "$titleText. $descText"

        // Tell the engine to speak and flush any previous speech queue
        tts?.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, "")
        Toast.makeText(this, "🔊 Playing Audio...", Toast.LENGTH_SHORT).show()
    }

    // Shut down TTS when the app page closes to save battery/memory
    override fun onDestroy() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onDestroy()
    }

    // 6. FETCH AND PARSE ML JSON DATA
    private fun fetchMeasurements(childId: String) {
        db.collection("measurements")
            .whereEqualTo("childId", childId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val mlDataJsonString = document.getString("mlData")

                    if (mlDataJsonString != null) {
                        try {
                            val jsonObject = JSONObject(mlDataJsonString)
                            val height = jsonObject.getDouble("heightCm")
                            val weight = jsonObject.getDouble("weightKg")
                            val bmi = jsonObject.getDouble("bmi")
                            val nutritionStatus = jsonObject.getString("nutritionStatus")

                            tvParentHeight.text = "$height cm"
                            tvParentWeight.text = "$weight kg"
                            tvParentBMI.text = bmi.toString()

                            tvStatusTitle.text = "Overall Health Status: $nutritionStatus"

                            if (nutritionStatus.equals("Normal", ignoreCase = true)) {
                                tvStatusDesc.text = "Great job! Your child is growing well. Keep maintaining a balanced diet and regular physical activity."
                            } else {
                                tvStatusDesc.text = "We noticed some irregularities in the growth curve. Please consult with your Anganwadi worker."
                            }

                        } catch (e: Exception) {
                            Log.e("Dashboard", "Error parsing ML JSON: ${e.message}")
                        }
                    }
                } else {
                    Toast.makeText(this, "No health measurements found yet.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load health data.", Toast.LENGTH_SHORT).show()
            }
    }
}