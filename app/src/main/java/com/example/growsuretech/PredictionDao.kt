package com.example.growsuretech

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PredictionDao {
    @Insert
    suspend fun insertPrediction(prediction: PredictionEntity)

    @Query("SELECT * FROM offline_predictions")
    suspend fun getAllUnsynced(): List<PredictionEntity>

    @Query("DELETE FROM offline_predictions WHERE id = :id")
    suspend fun deleteSyncedPrediction(id: Int)
}