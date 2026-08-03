package com.example.growsuretech

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.random.Random

class ParentAnalyticsActivity : AppCompatActivity() {

    // Connect to the UI
    private lateinit var lineChart: LineChart
    private lateinit var tvAnalyticsTitle: TextView
    private lateinit var tvInsight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_analytics)

        // Match the IDs from the XML
        lineChart = findViewById(R.id.lineChart)
        tvAnalyticsTitle = findViewById(R.id.tvAnalyticsTitle)
        tvInsight = findViewById(R.id.tvInsight)

        // 1. CATCH THE ID FROM THE DASHBOARD
        val childId = intent.getStringExtra("CHILD_ID") ?: "UNKNOWN_ID"

        tvAnalyticsTitle.text = "Child Growth History (ID: $childId)"

        // 2. GENERATE A RANDOM CHART (Bypassing Firebase for testing!)
        generateRandomChartData()
    }

    private fun generateRandomChartData() {
        val entries = ArrayList<Entry>()

        // Start at a realistic base weight (e.g., 12.0 kg)
        var currentWeight = 12.0f

        // Generate 6 random data points simulating months of growth
        for (month in 0..5) {
            entries.add(Entry(month.toFloat(), currentWeight))

            // Add a random weight gain between 0.3kg and 1.2kg for the next month
            val randomGain = Random.nextFloat() * (1.2f - 0.3f) + 0.3f
            currentWeight += randomGain
        }

        // 3. DRAW THE CHART
        setupChart(entries)

        // Update the insight text dynamically based on the final random weight
        val finalWeightFormatted = String.format("%.1f", currentWeight)
        tvInsight.text = "Insight: The child's latest recorded weight is $finalWeightFormatted kg."
    }

    // 4. CONFIGURE CHART VISUALS
    private fun setupChart(entries: ArrayList<Entry>) {
        val dataSet = LineDataSet(entries, "Weight History (kg)")

        // Customize colors and styles
        dataSet.color = Color.parseColor("#4CAF50")
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f
        dataSet.lineWidth = 4f
        dataSet.circleRadius = 6f
        dataSet.setCircleColor(Color.parseColor("#388E3C"))

        // 🚀 PRO-TIP: This makes the line perfectly smooth and curvy instead of jagged!
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.parseColor("#A5D6A7")

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Clean up the chart axes for a beautiful UI
        lineChart.description.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.granularity = 1f
        lineChart.axisRight.isEnabled = false
        lineChart.legend.textSize = 14f

        // Add a smooth animation when the page opens
        lineChart.animateX(1200)

        // Refresh the chart to show the data
        lineChart.invalidate()
    }
}