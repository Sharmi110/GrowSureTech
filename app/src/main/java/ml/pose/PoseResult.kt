package ml.pose

data class PoseResult(
    val bodyPixels: Float,
    val estimatedHeightCm: Float,
    val confidence: Float
)