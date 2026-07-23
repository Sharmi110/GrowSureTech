package ml.pose

import android.content.Context
import android.graphics.Bitmap
import ml.bmi.BMICalculator
import ml.nutrition.NutritionClassifier
import ml.result.AIResult
import ml.weight.WeightPredictor

class AITestPipeline {

    fun runPipeline(
        context: Context,
        bitmap: Bitmap,
        age: Int
    ): AIResult {

        // Initialize MediaPipe
        val detector = RealPoseDetector(context)
        detector.initialize()

        // Detect pose from captured image
        val landmarks = detector.detectPose(bitmap)

        if (landmarks == null) {
            return AIResult(
                heightCm = 0f,
                weightKg = 0f,
                bmi = 0f,
                nutritionStatus = "No Pose Detected",
                confidence = 0f
            )
        }

        // Process landmarks
        val processor = PoseProcessor()

        val poseResult = processor.processLandmarks(
            landmarks
        )

        val height = poseResult.estimatedHeightCm

        // Predict weight using REAL age
        val weight = WeightPredictor().predictWeight(
            height,
            age
        )

        // Calculate BMI
        val bmi = BMICalculator().calculate(
            height,
            weight
        )

        // Nutrition Classification
        val nutrition = NutritionClassifier().classify(
            bmi
        )

        val confidence = poseResult.confidence

        return AIResult(
            heightCm = height,
            weightKg = weight,
            bmi = bmi,
            nutritionStatus = nutrition,
            confidence = confidence
        )
    }
}