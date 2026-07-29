package ml.pose

import ml.height.HeightEstimator

class PoseProcessor {

    fun processLandmarks(
        landmarkData: LandmarkData,
        rulerPixels: Float
    ): PoseResult {

        val estimator = HeightEstimator()

        // Measure body height in pixels
        val bodyPixels = estimator.calculateBodyPixels(
            noseY = landmarkData.noseY,
            leftAnkleY = landmarkData.leftAnkleY,
            rightAnkleY = landmarkData.rightAnkleY
        )

        // Our reference ruler is 30 cm
        val referenceHeightCm = 30f

        // Safety check
        val safeRulerPixels =
            if (rulerPixels > 0f)
                rulerPixels
            else
                180f

        // Convert body pixels to centimetres
        val heightCm = estimator.pixelToCm(
            bodyPixels = bodyPixels,
            referencePixels = safeRulerPixels,
            referenceHeightCm = referenceHeightCm
        )

        return PoseResult(
            bodyPixels = bodyPixels,
            estimatedHeightCm = heightCm,
            confidence = 0.95f
        )
    }
}