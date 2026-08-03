package ml.height

class HeightEstimator {

    /**
     * MediaPipe Y coordinates are normalized from 0.0 to 1.0.
     * Convert the vertical distance into actual image pixels.
     */
    fun calculateBodyPixels(
        noseY: Float,
        leftAnkleY: Float,
        rightAnkleY: Float,
        imageHeightPixels: Int
    ): Float {

        if (imageHeightPixels <= 0) return 0f

        val averageAnkleY = (leftAnkleY + rightAnkleY) / 2f

        val normalizedBodyHeight = averageAnkleY - noseY

        if (normalizedBodyHeight <= 0f) return 0f

        return normalizedBodyHeight * imageHeightPixels
    }

    /**
     * Convert body pixels into centimetres using a real reference object.
     */
    fun pixelToCm(
        bodyPixels: Float,
        referencePixels: Float,
        referenceHeightCm: Float
    ): Float {

        if (bodyPixels <= 0f || referencePixels <= 0f) {
            return 0f
        }

        return (bodyPixels / referencePixels) * referenceHeightCm
    }
}