package ml.pose

import android.graphics.Bitmap

class MockPoseDetector {

    fun detectPose(
        bitmap: Bitmap
    ): LandmarkData {

        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        android.util.Log.d(
            "MOCK_POSE",
            "Image Size = ${imageWidth} x ${imageHeight}"
        )

        return LandmarkData(
            noseX = imageWidth * 0.5f,
            noseY = imageHeight * 0.2f,

            leftAnkleX = imageWidth * 0.45f,
            leftAnkleY = imageHeight * 0.9f,

            rightAnkleX = imageWidth * 0.55f,
            rightAnkleY = imageHeight * 0.9f
        )
    }
}