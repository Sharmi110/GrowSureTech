package ml.pose

import ml.height.HeightEstimator

class HeightEstimationTest {

    fun runTest() {

        val estimator = HeightEstimator()

        // Simulated normalized MediaPipe coordinates.
        val bodyPixels = estimator.calculateBodyPixels(
            noseY = 0.10f,
            leftAnkleY = 0.80f,
            rightAnkleY = 0.80f,
            imageHeightPixels = 1000
        )

        // Test-only reference values.
        val referencePixels = 500f
        val referenceHeightCm = 100f

        val estimatedHeight = estimator.pixelToCm(
            bodyPixels = bodyPixels,
            referencePixels = referencePixels,
            referenceHeightCm = referenceHeightCm
        )

        println("Body Pixels = $bodyPixels")
        println("Estimated Height = $estimatedHeight cm")
    }
}