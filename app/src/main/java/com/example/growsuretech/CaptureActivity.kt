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

        // 1. UNPACK THE DATA SENT FROM REGISTER ACTIVITY
        currentChildId = intent.getStringExtra("CHILD_ID") ?: "UNKNOWN"
        childDob = intent.getStringExtra("CHILD_DOB") ?: ""
        childGender = intent.getStringExtra("CHILD_GENDER") ?: ""

        btnCapture = findViewById(R.id.btnCapture)
        btnAnalyze = findViewById(R.id.btnAnalyze)
        ivPreview = findViewById(R.id.ivPreview)

        // 2. OPEN CAMERA
        btnCapture.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 100)
        }

        // 3. TRIGGER ML AND SAVE
        btnAnalyze.setOnClickListener {
            if (capturedBitmap == null) {
                Toast.makeText(this, "Please capture a photo first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            processWithML(capturedBitmap!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            capturedBitmap = data?.extras?.get("data") as Bitmap
            ivPreview.setImageBitmap(capturedBitmap)
        }
    }

    private fun processWithML(bitmap: Bitmap) {
        Toast.makeText(this, "Running ML Model...", Toast.LENGTH_SHORT).show()

        // ⚠️ HEY ML DEVELOPER:
        // You now have access to 'bitmap' (the photo), 'childDob', and 'childGender'

        // Android Dev: Updated to perfectly match the ML output contract (replaced confidence with bmi)
        val mlResultJson = """
            {
                "heightCm": 95.5,
                "weightKg": 14.2,
                "nutritionStatus": "Normal",
                "bmi": 15.6
            }
        """.trimIndent()

        // 4. CHECK NETWORK AND SAVE
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

        db.collection("measurements").add(measurementData)
            .addOnSuccessListener {
                Toast.makeText(this, "Saved to Firebase (Online!)", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Firebase failed, saving offline...", Toast.LENGTH_SHORT).show()
                saveToRoom(mlJson)
            }
    }

    private fun saveToRoom(mlJson: String) {
        val prediction = PredictionEntity(
            childId = currentChildId,
            mlResultJson = mlJson
        )

        CoroutineScope(Dispatchers.IO).launch {
            val roomDb = AppDatabase.getDatabase(this@CaptureActivity).predictionDao()
            roomDb.insertPrediction(prediction)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@CaptureActivity, "Saved Locally (Offline Mode)", Toast.LENGTH_LONG).show()
            }
        }
    }
}