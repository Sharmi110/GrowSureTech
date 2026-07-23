package ml.pose

import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker

class PoseDetector {

    private var poseLandmarker: PoseLandmarker? = null

    fun initialize() {
        println("MediaPipe Pose Detector Initialized")
    }

    fun detectPose() {
        println("Pose Detection Started")
    }
}