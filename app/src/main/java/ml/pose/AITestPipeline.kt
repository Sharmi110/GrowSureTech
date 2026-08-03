package ml.pose

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import ml.bmi.BMICalculator
import ml.nutrition.NutritionClassifier
import ml.opencv.OpenCVRulerDetector
import ml.result.AIResult
import ml.weight.WeightPredictor

class AITestPipeline {

    companion object {
        // Our physical calibration strip is 1 metre.
        private const val REFERENCE_STRIP_LENGTH_CM = 100f
    }

    fun runPipeline(
        context: Context,
        bitmap: Bitmap,
        age: Int
    ): AIResult {

        // --------------------------------
        // STEP 1: Initialize MediaPipe
        // --------------------------------
        val detector = RealPoseDetector(context)
        detector.initialize()

        // --------------------------------
        // STEP 2: Detect body landmarks
        // --------------------------------
        val landmarks = detector.detectPose(bitmap)

        if (landmarks == null) {
            Log.e(
                "ML_PIPELINE",
                "No human pose detected"
            )

            return AIResult(
                heightCm = 0f,
                weightKg = 0f,
                bmi = 0f,
                nutritionStatus = "No Pose Detected",
                confidence = 0f
            )
        }

        // --------------------------------
        // STEP 3: Detect the 1 m reference strip
        // --------------------------------
        val rulerDetector = OpenCVRulerDetector()

        val rulerPixels = rulerDetector.detectRuler(bitmap)

        Log.d(
            "RULER_TEST",
            "Detected reference strip pixels = $rulerPixels"
        )

        // Do NOT use a fake fallback value.
        if (rulerPixels <= 0f) {

            Log.e(
                "ML_PIPELINE",
                "Reference strip was not detected"
            )

            return AIResult(
                heightCm = 0f,
                weightKg = 0f,
                bmi = 0f,
                nutritionStatus = "Reference Strip Not Detected",
                confidence = 0f
            )
        }

        // --------------------------------
        // STEP 4: Estimate height
        // --------------------------------
        val processor = PoseProcessor()

        val poseResult = processor.processLandmarks(
            landmarkData = landmarks,
            rulerPixels = rulerPixels,
            imageHeightPixels = bitmap.height,
            referenceHeightCm = REFERENCE_STRIP_LENGTH_CM
        )

        Log.d(
            "HEIGHT_TEST",
            "Body pixels = ${poseResult.bodyPixels}"
        )

        Log.d(
            "HEIGHT_TEST",
            "Estimated height = ${poseResult.estimatedHeightCm} cm"
        )

        val height = poseResult.estimatedHeightCm

        if (height <= 0f) {
            return AIResult(
                heightCm = 0f,
                weightKg = 0f,
                bmi = 0f,
                nutritionStatus = "Invalid Height Estimate",
                confidence = 0f
            )
        }

        // --------------------------------
        // STEP 5: Predict weight
        // --------------------------------
        val weight = WeightPredictor().predictWeight(
            heightCm = height,
            ageYears = age
        )

        Log.d(
            "WEIGHT_TEST",
            "Predicted weight = $weight kg"
        )

        if (weight <= 0f) {
            return AIResult(
                heightCm = height,
                weightKg = 0f,
                bmi = 0f,
                nutritionStatus = "Invalid Weight Estimate",
                confidence = 0f
            )
        }

        // --------------------------------
        // STEP 6: Calculate BMI
        // --------------------------------
        val bmi = BMICalculator().calculate(
            height,
            weight
        )

        Log.d(
            "BMI_TEST",
            "BMI = $bmi"
        )

        // --------------------------------
        // STEP 7: Nutrition classification
        // --------------------------------
        val nutrition = NutritionClassifier().classify(bmi)

        Log.d(
            "NUTRITION_TEST",
            "Nutrition status = $nutrition"
        )

        // --------------------------------
        // STEP 8: Final result
        // --------------------------------
        return AIResult(
            heightCm = height,
            weightKg = weight,
            bmi = bmi,
            nutritionStatus = nutrition,
            confidence = poseResult.confidence
        )
    }
}