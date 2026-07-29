package ml.opencv

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class OpenCVRulerDetector {

    fun detectRuler(bitmap: Bitmap): Float {

        // Bitmap → OpenCV Mat
        val original = Mat()
        Utils.bitmapToMat(bitmap, original)

        // Convert to Gray
        val gray = Mat()
        Imgproc.cvtColor(
            original,
            gray,
            Imgproc.COLOR_RGBA2GRAY
        )

        // Blur
        val blur = Mat()
        Imgproc.GaussianBlur(
            gray,
            blur,
            Size(5.0, 5.0),
            0.0
        )

        // Edge Detection
        val edges = Mat()
        Imgproc.Canny(
            blur,
            edges,
            50.0,
            150.0
        )

        // Find Contours
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()

        Imgproc.findContours(
            edges,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        var bestHeight = 0f
        var bestArea = 0f

        for (contour in contours) {

            val rect = Imgproc.boundingRect(contour)

            val width = rect.width.toFloat()
            val height = rect.height.toFloat()

            val area = width * height

            // Ignore tiny objects
            if (area < 2500f)
                continue

            // Ignore horizontal rectangles
            if (height < width * 2f)
                continue

            // Ignore extremely thin objects
            if (width < 10f)
                continue

            // Keep the largest vertical rectangle
            if (area > bestArea) {
                bestArea = area
                bestHeight = height
            }
        }

        // Release memory
        original.release()
        gray.release()
        blur.release()
        edges.release()
        hierarchy.release()

        return bestHeight
    }
}