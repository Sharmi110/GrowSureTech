package ml.weight

class WeightPredictor {

    fun predictWeight(
        heightCm: Float,
        ageYears: Int
    ): Float {

        return when {

            ageYears <= 2 ->
                (heightCm - 50f) * 0.30f

            ageYears <= 5 ->
                (heightCm - 60f) * 0.35f

            else ->
                (heightCm - 70f) * 0.40f
        }
    }
}