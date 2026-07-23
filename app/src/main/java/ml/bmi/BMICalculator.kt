package ml.bmi

class BMICalculator {

    fun calculate(
        heightCm: Float,
        weightKg: Float
    ): Float {
        val heightMeter = heightCm / 100f
        return weightKg / (heightMeter * heightMeter)
    }
}