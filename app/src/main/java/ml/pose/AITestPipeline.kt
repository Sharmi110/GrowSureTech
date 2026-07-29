package ml.pose

import android.content.Context
import android.graphics.Bitmap
import ml.bmi.BMICalculator
import ml.nutrition.NutritionClassifier
import ml.opencv.OpenCVRulerDetector
import ml.result.AIResult
import ml.weight.WeightPredictor

class AITestPipeline {

    fun runPipeline(
        context: Context,
        bitmap: Bitmap,
        age: Int
    ): AIResult {

        // -------------------------------
        // STEP 1: Initialize MediaPipe
        // -------------------------------
        val detector = RealPoseDetector(context)
        detector.initialize()

        // -------------------------------
        // STEP 2: Detect body landmarks
        // -------------------------------
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

        // -------------------------------
        // STEP 3: Detect reference ruler
        // -------------------------------
        val rulerDetector = OpenCVRulerDetector()

        var rulerPixels = rulerDetector.detectRuler(bitmap)
        android.util.Log.d("RULER_TEST", "Detected ruler pixels = $rulerPixels")

        // Safety fallback
        if (rulerPixels <= 0f) {
            rulerPixels = 180f
        }

        // -------------------------------
        // STEP 4: Estimate height
        // -------------------------------
        val processor = PoseProcessor()

        val poseResult = processor.processLandmarks(
            landmarkData = landmarks,
            rulerPixels = rulerPixels
        )
        android.util.Log.d("HEIGHT_TEST", "Body pixels = ${poseResult.bodyPixels}")
        android.util.Log.d("HEIGHT_TEST", "Estimated height = ${poseResult.estimatedHeightCm}")

        val height = poseResult.estimatedHeightCm

        // -------------------------------
        // STEP 5: Predict weight
        // -------------------------------
        val weight = WeightPredictor().predictWeight(
            heightCm = height,
            ageYears = age
        )

        // -------------------------------
        // STEP 6: Calculate BMI
        // -------------------------------
        val bmi = BMICalculator().calculate(
            height,
            weight
        )

        // -------------------------------
        // STEP 7: Nutrition classification
        // -------------------------------
        val nutrition = NutritionClassifier().classify(bmi)

        return AIResult(
            heightCm = height,
            weightKg = weight,
            bmi = bmi,
            nutritionStatus = nutrition,
            confidence = poseResult.confidence
        )
    }
}