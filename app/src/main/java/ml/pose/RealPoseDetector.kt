package ml.pose

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker

class RealPoseDetector(
    private val context: Context
) {

    private var poseLandmarker: PoseLandmarker? = null

    fun initialize() {

        val baseOptions = BaseOptions.builder()
            .setModelAssetPath("pose_landmarker_lite.task")
            .build()

        val options = PoseLandmarker.PoseLandmarkerOptions.builder()
            .setBaseOptions(baseOptions)
            .setRunningMode(RunningMode.IMAGE)
            .build()

        poseLandmarker = PoseLandmarker.createFromOptions(
            context,
            options
        )
    }

    fun detectPose(bitmap: Bitmap): LandmarkData? {

        val mpImage = BitmapImageBuilder(bitmap).build()

        val result = poseLandmarker?.detect(mpImage)

        if (result == null || result.landmarks().isEmpty()) {
            Log.e("MEDIAPIPE_RESULT", "No pose detected")
            return null
        }

        val landmarks = result.landmarks()[0]

        val nose = landmarks[0]
        val leftAnkle = landmarks[27]
        val rightAnkle = landmarks[28]

        Log.d("MEDIAPIPE_RESULT", "Nose = (${nose.x()}, ${nose.y()})")
        Log.d("MEDIAPIPE_RESULT", "Left Ankle = (${leftAnkle.x()}, ${leftAnkle.y()})")
        Log.d("MEDIAPIPE_RESULT", "Right Ankle = (${rightAnkle.x()}, ${rightAnkle.y()})")

        return LandmarkData(
            noseX = nose.x(),
            noseY = nose.y(),

            leftAnkleX = leftAnkle.x(),
            leftAnkleY = leftAnkle.y(),

            rightAnkleX = rightAnkle.x(),
            rightAnkleY = rightAnkle.y()
        )
    }
}