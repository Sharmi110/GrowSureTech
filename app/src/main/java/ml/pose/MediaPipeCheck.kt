package ml.pose

import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode

class MediaPipeCheck {

    fun check(): String {

        val mode = RunningMode.IMAGE

        return mode.name
    }
}