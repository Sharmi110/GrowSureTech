package com.example.growsuretech

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.utils.AgeCalculator

class CaptureActivity : AppCompatActivity() {

    private lateinit var btnCapture: Button
    private lateinit var btnAnalyze: Button
    private lateinit var ivPreview: ImageView
    private var capturedBitmap: Bitmap? = null
    private lateinit var db: FirebaseFirestore

    private var currentChildId = ""
    private var childDob = ""
    private var childGender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        db = FirebaseFirestore.getInstance()

        // Get data from RegisterActivity
        currentChildId = intent.getStringExtra("CHILD_ID") ?: "UNKNOWN"
        childDob = intent.getStringExtra("CHILD_DOB") ?: ""
        childGender = intent.getStringExtra("CHILD_GENDER") ?: ""

        btnCapture = findViewById(R.id.btnCapture)
        btnAnalyze = findViewById(R.id.btnAnalyze)
        ivPreview = findViewById(R.id.ivPreview)

        btnCapture.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 100)
        }

        btnAnalyze.setOnClickListener {
            if (capturedBitmap == null) {
                Toast.makeText(
                    this,
                    "Please capture a photo first!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            processWithML(capturedBitmap!!)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            capturedBitmap = data?.extras?.get("data") as Bitmap
            ivPreview.setImageBitmap(capturedBitmap)
        }
    }

    private fun processWithML(bitmap: Bitmap) {

        Toast.makeText(
            this,
            "Running ML Model...",
            Toast.LENGTH_SHORT
        ).show()

        // Calculate child's age from DOB
        val age = AgeCalculator.calculateAge(childDob)

        // Run AI Pipeline
        val aiResult = ml.pose.AITestPipeline().runPipeline(
            this,
            bitmap,
            age
        )

        // Convert AI Result to JSON
        val mlResultJson = """
        {
            "heightCm": ${aiResult.heightCm},
            "weightKg": ${aiResult.weightKg},
            "nutritionStatus": "${aiResult.nutritionStatus}",
            "bmi": ${aiResult.bmi},
            "confidence": ${aiResult.confidence}
        }
        """.trimIndent()

        saveData(mlResultJson)
    }

    private fun saveData(mlJson: String) {

        if (NetworkUtils.isOnline(this)) {
            saveToFirebase(mlJson)
        } else {
            saveToRoom(mlJson)
        }
    }

    private fun saveToFirebase(mlJson: String) {

        val measurementData = hashMapOf(
            "childId" to currentChildId,
            "mlData" to mlJson,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("measurements")
            .add(measurementData)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Saved to Firebase (Online!)",
                    Toast.LENGTH_LONG
                ).show()

            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Firebase failed, saving offline...",
                    Toast.LENGTH_SHORT
                ).show()

                saveToRoom(mlJson)
            }
    }

    private fun saveToRoom(mlJson: String) {

        val prediction = PredictionEntity(
            childId = currentChildId,
            mlResultJson = mlJson
        )

        CoroutineScope(Dispatchers.IO).launch {

            val roomDb =
                AppDatabase.getDatabase(this@CaptureActivity)
                    .predictionDao()

            roomDb.insertPrediction(prediction)

            withContext(Dispatchers.Main) {

                Toast.makeText(
                    this@CaptureActivity,
                    "Saved Locally (Offline Mode)",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}