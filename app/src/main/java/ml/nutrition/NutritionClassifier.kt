package ml.nutrition

class NutritionClassifier {

    fun classify(bmi: Float): String {
        return when {
            bmi < 14f -> "Severely Underweight"
            bmi < 18.5f -> "Underweight"
            bmi < 25f -> "Normal"
            bmi < 30f -> "Overweight"
            else -> "Obese"
        }
    }
}