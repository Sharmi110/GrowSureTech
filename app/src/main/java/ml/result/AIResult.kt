package ml.result

data class AIResult(
    val heightCm: Float,
    val weightKg: Float,
    val bmi: Float,
    val nutritionStatus: String,
    val confidence: Float
)