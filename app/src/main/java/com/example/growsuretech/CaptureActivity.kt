package com.example.growsuretech

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.pose.AITestPipeline
import ml.utils.AgeCalculator

class CaptureActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_REQUEST_CODE = 100
        private const val CAMERA_PERMISSION_CODE = 101
    }

    private lateinit var btnCapture: Button
    private lateinit var btnSubmit: Button
    private lateinit var imgPreview: ImageView
    private var capturedBitmap: Bitmap? = null

    private lateinit var db: FirebaseFirestore

    private var currentChildId = ""
    private var childDob = ""
    private var childGender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        db = FirebaseFirestore.getInstance()

        currentChildId = intent.getStringExtra("CHILD_ID") ?: "UNKNOWN"
        childDob = intent.getStringExtra("CHILD_DOB") ?: ""
        childGender = intent.getStringExtra("CHILD_GENDER") ?: ""

        btnCapture = findViewById(R.id.btnCapture)
        btnSubmit = findViewById(R.id.btnSubmit)
        imgPreview = findViewById(R.id.imgPreview)


        // 2. OPEN CAMERA WITH PERMISSION CHECK
        btnCapture.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                openCamera()
            } else {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )


            }
        }


        // 3. TRIGGER ML AND SAVE
        btnSubmit.setOnClickListener {

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



    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (requestCode == CAMERA_PERMISSION_CODE) {

            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {

                Toast.makeText(
                    this,
                    "Camera permission denied.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE &&
            resultCode == RESULT_OK
        ) {
            capturedBitmap = data?.extras?.get("data") as? Bitmap

            if (capturedBitmap != null) {
                imgPreview.setImageBitmap(capturedBitmap)
            } else {
                Toast.makeText(
                    this,
                    "Failed to load image from camera. Try again.",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }

    private fun processWithML(bitmap: Bitmap) {

                Toast.makeText(
                    this,
                    "Running ML Model...",
                    Toast.LENGTH_SHORT
                ).show()

                val age = AgeCalculator.calculateAge(childDob)

                val aiResult = AITestPipeline().runPipeline(
                    this,
                    bitmap,
                    age
                )

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
                    "Saved to Firebase!",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, TeacherDashboardActivity::class.java)
                intent.putExtra("CHILD_ID", currentChildId)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                startActivity(intent)
                finish()

            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Saving Offline...",
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


            val dao = AppDatabase
                .getDatabase(this@CaptureActivity)
                .predictionDao()

            dao.insertPrediction(prediction)
            SyncUtils.schedulePredictionSync(this@CaptureActivity)
            withContext(Dispatchers.Main) {

                Toast.makeText(
                    this@CaptureActivity,
                    "Saved Offline",
                    Toast.LENGTH_LONG
                ).show()
                // 🚀 THE FIX: Navigate even if saved offline
                val intent = Intent(this@CaptureActivity, TeacherDashboardActivity::class.java)
                intent.putExtra("CHILD_ID", currentChildId)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}