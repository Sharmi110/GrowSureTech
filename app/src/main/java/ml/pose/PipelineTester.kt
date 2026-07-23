package ml.pose

class PipelineTester {

    fun runTest() {

        val landmarkData = LandmarkData(
            noseX = 250f,
            noseY = 100f,

            leftAnkleX = 230f,
            leftAnkleY = 800f,

            rightAnkleX = 270f,
            rightAnkleY = 810f
        )

        val processor = PoseProcessor()

        val result = processor.processLandmarks(
            landmarkData
        )

        println("Body Pixels = ${result.bodyPixels}")
        println("Height = ${result.estimatedHeightCm}")
        println("Confidence = ${result.confidence}")
    }
}