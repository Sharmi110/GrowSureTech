package ml.pose

import ml.height.HeightEstimator

class HeightEstimationTest {

    fun runTest() {

        val estimator = HeightEstimator()

        val bodyPixels = estimator.calculateBodyPixels(
            noseY = 120f,
            leftAnkleY = 850f,
            rightAnkleY = 848f
        )

        val estimatedHeight = estimator.pixelToCm(
            bodyPixels = bodyPixels,
            referencePixels = 900f,
            referenceHeightCm = 180f
        )

        println("Body Pixels = $bodyPixels")
        println("Estimated Height = $estimatedHeight cm")
    }
}