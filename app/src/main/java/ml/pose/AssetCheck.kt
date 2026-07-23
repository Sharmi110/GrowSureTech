package ml.pose

import android.content.Context

class AssetCheck {

    fun checkModel(
        context: Context
    ): Boolean {

        return try {

            context.assets.open(
                "pose_landmarker_lite.task"
            )

            true

        } catch (e: Exception) {

            false
        }
    }
}