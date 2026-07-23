package com.example.growsuretech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChildTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.child_type_activity)

        // AUTOMATIC STEP 5: Silent check & sync background database on screen open
        syncOfflineDataToFirebase()

        // Updated IDs to match your new XML configuration
        val btnNew = findViewById<Button>(R.id.btnNew)
        val btnExisting = findViewById<Button>(R.id.btnExisting)

        // ROUTE 1: New Child Registration Flow
        btnNew.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // ROUTE 2: Existing Child Dashboard Flow
        btnExisting.setOnClickListener {
            val intent = Intent(this, TeacherDashboardActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Step 5 Sync Function:
     * When online, it background-fetches locally stored ML measurements
     * from Room DB, safely uploads them to Firestore, and clears the cache.
     */
    private fun syncOfflineDataToFirebase() {
        if (NetworkUtils.isOnline(this)) {

            CoroutineScope(Dispatchers.IO).launch {
                val roomDb = AppDatabase.getDatabase(this@ChildTypeActivity).predictionDao()
                val offlineRecords = roomDb.getAllUnsynced()

                if (offlineRecords.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ChildTypeActivity, "Syncing offline data to cloud...", Toast.LENGTH_SHORT).show()
                    }

                    for (record in offlineRecords) {
                        val data = hashMapOf(
                            "childId" to record.childId,
                            "mlData" to record.mlResultJson,
                            "timestamp" to record.timestamp
                        )

                        FirebaseFirestore.getInstance().collection("measurements")
                            .add(data)
                            .addOnSuccessListener {
                                CoroutineScope(Dispatchers.IO).launch {
                                    roomDb.deleteSyncedPrediction(record.id)
                                }
                            }
                    }
                }
            }
        }
    }
}