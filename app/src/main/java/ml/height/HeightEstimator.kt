package ml.height

class HeightEstimator {

    fun calculateBodyPixels(
        noseY: Float,
        leftAnkleY: Float,
        rightAnkleY: Float
    ): Float {

        val averageAnkleY = (leftAnkleY + rightAnkleY) / 2f

        return averageAnkleY - noseY
    }

    fun pixelToCm(
        bodyPixels: Float,
        referencePixels: Float,
        referenceHeightCm: Float
    ): Float {

        if (referencePixels <= 0f) return 0f

        return (bodyPixels / referencePixels) * referenceHeightCm
    }
}