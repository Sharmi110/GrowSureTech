package com.example.growsuretech

// We use an 'object' so we can access this math from anywhere in the app
object HealthCalculator {

    // A data class to return both the BMI number and the text status at the same time
    data class HealthResult(val bmi: Double, val status: String)

    fun calculateHealthStatus(weightKg: Double, heightCm: Double): HealthResult {
        // 1. Convert height from centimeters to meters
        val heightMeters = heightCm / 100.0

        // 2. Calculate BMI: weight / (height * height)
        val bmi = weightKg / (heightMeters * heightMeters)

        // 3. Round to one decimal place for a cleaner display
        val roundedBmi = Math.round(bmi * 10.0) / 10.0

        // 4. Determine the WHO Status Category
        val status = when {
            roundedBmi < 16.0 -> "Severely Underweight"
            roundedBmi in 16.0..18.4 -> "Underweight"
            roundedBmi in 18.5..24.9 -> "Normal"
            roundedBmi in 25.0..29.9 -> "Overweight"
            else -> "Obese"
        }

        return HealthResult(roundedBmi, status)
    }
}