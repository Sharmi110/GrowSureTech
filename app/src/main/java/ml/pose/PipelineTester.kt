package ml.pose

class PipelineTester {

    fun runTest() {

        // Simulated MediaPipe normalized coordinates.
        val landmarkData = LandmarkData(
            noseX = 0.50f,
            noseY = 0.10f,

            leftAnkleX = 0.46f,
            leftAnkleY = 0.80f,

            rightAnkleX = 0.54f,
            rightAnkleY = 0.81f
        )

        val processor = PoseProcessor()

        // Test-only values.
        // In the real app these come from:
        // rulerPixels -> OpenCV
        // imageHeightPixels -> bitmap.height
        val rulerPixels = 500f
        val imageHeightPixels = 1000
        val referenceHeightCm = 100f

        val result = processor.processLandmarks(
            landmarkData = landmarkData,
            rulerPixels = rulerPixels,
            imageHeightPixels = imageHeightPixels,
            referenceHeightCm = referenceHeightCm
        )

        println("Body Pixels = ${result.bodyPixels}")
        println("Estimated Height = ${result.estimatedHeightCm} cm")
        println("Confidence = ${result.confidence}")
    }
}