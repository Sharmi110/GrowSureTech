package ml.opencv

import android.graphics.Bitmap
import android.util.Log
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.math.max

class OpenCVRulerDetector {

    fun detectRuler(bitmap: Bitmap): Float {

        // ----------------------------------------
        // 1. Make sure OpenCV native library is loaded
        // ----------------------------------------
        if (!OpenCVLoader.initLocal()) {
            Log.e(
                "OPENCV_INIT",
                "Failed to initialize OpenCV native library"
            )
            return 0f
        }

        if (bitmap.width <= 0 || bitmap.height <= 0) {
            Log.e(
                "OPENCV_INIT",
                "Invalid bitmap dimensions"
            )
            return 0f
        }

        // ----------------------------------------
        // 2. Bitmap -> OpenCV Mat
        // ----------------------------------------
        val original = Mat()

        try {
            Utils.bitmapToMat(bitmap, original)

            // ----------------------------------------
            // 3. Convert to grayscale
            // ----------------------------------------
            val gray = Mat()

            Imgproc.cvtColor(
                original,
                gray,
                Imgproc.COLOR_RGBA2GRAY
            )

            // ----------------------------------------
            // 4. Blur
            // ----------------------------------------
            val blurred = Mat()

            Imgproc.GaussianBlur(
                gray,
                blurred,
                Size(5.0, 5.0),
                0.0
            )

            // ----------------------------------------
            // 5. Canny edge detection
            // ----------------------------------------
            val edges = Mat()

            Imgproc.Canny(
                blurred,
                edges,
                50.0,
                150.0
            )

            // ----------------------------------------
            // 6. Find contours
            // ----------------------------------------
            val contours = ArrayList<MatOfPoint>()
            val hierarchy = Mat()

            Imgproc.findContours(
                edges,
                contours,
                hierarchy,
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE
            )

            // ----------------------------------------
            // 7. Find best vertical strip candidate
            // ----------------------------------------
            var bestHeight = 0f
            var bestScore = 0f

            val imageHeight = bitmap.height.toFloat()
            val imageWidth = bitmap.width.toFloat()

            for (contour in contours) {

                val rect = Imgproc.boundingRect(contour)

                val width = rect.width.toFloat()
                val height = rect.height.toFloat()

                if (width <= 0f || height <= 0f) {
                    continue
                }

                val area = width * height
                val aspectRatio = height / width
                val heightRatio = height / imageHeight

                if (area < 2500f) {
                    continue
                }

                if (aspectRatio < 3f) {
                    continue
                }

                if (width < 10f) {
                    continue
                }

                if (heightRatio < 0.20f) {
                    continue
                }

                if (width > imageWidth * 0.30f) {
                    continue
                }

                val normalizedHeight = height / imageHeight
                val aspectBonus =
                    max(0f, (aspectRatio - 3f) / 10f)

                val score = normalizedHeight + aspectBonus

                if (score > bestScore) {
                    bestScore = score
                    bestHeight = height
                }
            }

            Log.d(
                "OPENCV_RULER",
                "Detected reference strip pixels = $bestHeight"
            )

            // ----------------------------------------
            // 8. Release native memory
            // ----------------------------------------
            gray.release()
            blurred.release()
            edges.release()
            hierarchy.release()

            for (contour in contours) {
                contour.release()
            }

            return bestHeight

        } catch (e: Exception) {

            Log.e(
                "OPENCV_RULER",
                "OpenCV processing failed",
                e
            )

            return 0f

        } finally {
            original.release()
        }
    }
}