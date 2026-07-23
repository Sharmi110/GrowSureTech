package ml.weight

class WeightPredictionTest {

    fun runTest() {

        val predictor = WeightPredictor()

        val predictedWeight = predictor.predictWeight(
            heightCm = 145.8f,
            ageYears = 5
        )

        println("Predicted Weight = $predictedWeight kg")
    }
}