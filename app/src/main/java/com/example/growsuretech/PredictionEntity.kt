package com.example.growsuretech

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_predictions")
data class PredictionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val childId: String,
    val mlResultJson: String,
    val timestamp: Long = System.currentTimeMillis()
)