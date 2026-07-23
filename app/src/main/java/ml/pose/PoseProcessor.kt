package ml.pose

import ml.height.HeightEstimator

class PoseProcessor {

    fun processLandmarks(
        landmarkData: LandmarkData
    ): PoseResult {

        val estimator = HeightEstimator()

        val bodyPixels = estimator.calculateBodyPixels(
            noseY = landmarkData.noseY,
            leftAnkleY = landmarkData.leftAnkleY,
            rightAnkleY = landmarkData.rightAnkleY
        )

        // Temporary reference for prototype
        // Later this will come from the detected 30 cm ruler
        val referencePixels = 180f
        val referenceHeightCm = 30f

        val heightCm = estimator.pixelToCm(
            bodyPixels = bodyPixels,
            referencePixels = referencePixels,
            referenceHeightCm = referenceHeightCm
        )

        return PoseResult(
            bodyPixels = bodyPixels,
            estimatedHeightCm = heightCm,
            confidence = 0.95f
        )
    }
}