package com.example.growsuretech

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PredictionSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val dao = AppDatabase
            .getDatabase(applicationContext)
            .predictionDao()

        val firestore = FirebaseFirestore.getInstance()

        val predictions = dao.getAllUnsynced()

        try {

            for (prediction in predictions) {

                val data = hashMapOf(
                    "childId" to prediction.childId,
                    "mlData" to prediction.mlResultJson,
                    "timestamp" to prediction.timestamp
                )

                firestore.collection("measurements")
                    .add(data)
                    .await()

                dao.deleteSyncedPrediction(prediction.id)
            }

            return Result.success()

        } catch (e: Exception) {

            return Result.retry()
        }
    }
}