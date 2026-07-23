package ml.nutrition

class NutritionTest {

    fun runTest() {

        val classifier = NutritionClassifier()

        val status = classifier.classify(
            bmi = 16.5f
        )

        println("Nutrition Status = $status")
    }
}