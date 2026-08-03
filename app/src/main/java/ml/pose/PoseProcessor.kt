package ml.pose

import ml.height.HeightEstimator

class PoseProcessor {

    fun processLandmarks(
        landmarkData: LandmarkData,
        rulerPixels: Float,
        imageHeightPixels: Int,
        referenceHeightCm: Float
    ): PoseResult {

        val estimator = HeightEstimator()

        // MediaPipe gives normalized Y coordinates (0.0 - 1.0).
        // Convert the body measurement to actual image pixels.
        val bodyPixels = estimator.calculateBodyPixels(
            noseY = landmarkData.noseY,
            leftAnkleY = landmarkData.leftAnkleY,
            rightAnkleY = landmarkData.rightAnkleY,
            imageHeightPixels = imageHeightPixels
        )

        // We cannot estimate height without a valid reference.
        if (bodyPixels <= 0f || rulerPixels <= 0f || referenceHeightCm <= 0f) {
            return PoseResult(
                bodyPixels = bodyPixels,
                estimatedHeightCm = 0f,
                confidence = 0f
            )
        }

        // Convert body pixels to centimetres using the physical
        // reference-strip length.
        val heightCm = estimator.pixelToCm(
            bodyPixels = bodyPixels,
            referencePixels = rulerPixels,
            referenceHeightCm = referenceHeightCm
        )

        // Prototype sanity check.
        // Reject clearly impossible measurements.
        if (heightCm !in 40f..220f) {
            return PoseResult(
                bodyPixels = bodyPixels,
                estimatedHeightCm = 0f,
                confidence = 0f
            )
        }

        return PoseResult(
            bodyPixels = bodyPixels,
            estimatedHeightCm = heightCm,
            confidence = 0.95f
        )
    }
}