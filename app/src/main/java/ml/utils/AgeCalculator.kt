package ml.utils

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

object AgeCalculator {

    private val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")

    fun calculateAge(dob: String): Int {

        return try {

            val birthDate = LocalDate.parse(
                dob,
                formatter
            )

            Period.between(
                birthDate,
                LocalDate.now()
            ).years

        } catch (e: Exception) {

            5

        }

    }
}