package com.example.growsuretech // Keep your unique package name!

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_admin_dashboard)

        setupChartHealthStatus()
        setupChartAvgBmi()
        setupChartUnderOver()
        setupChartHeightWeight()
        setupChartBmiDist()
        setupChartHealthTrend()
    }

    private fun setupChartHealthStatus() {
        val chart = findViewById<BarChart>(R.id.chartHealthStatus)

        // 3 Regions: Trichy(0), Coimbatore(1), Chennai(2)
        val healthy = BarDataSet(listOf(BarEntry(0f, 850f), BarEntry(1f, 760f), BarEntry(2f, 605f)), "Healthy")
        healthy.color = Color.parseColor("#4CAF50")

        val average = BarDataSet(listOf(BarEntry(0f, 420f), BarEntry(1f, 320f), BarEntry(2f, 290f)), "Average")
        average.color = Color.parseColor("#FF9800")

        val atRisk = BarDataSet(listOf(BarEntry(0f, 230f), BarEntry(1f, 180f), BarEntry(2f, 170f)), "At Risk")
        atRisk.color = Color.parseColor("#F44336")

        val data = BarData(healthy, average, atRisk)
        chart.data = data

        // Magic Math to group 3 bars together
        val groupSpace = 0.28f
        val barSpace = 0.04f
        val barWidth = 0.2f
        data.barWidth = barWidth

        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = 3f
        chart.groupBars(0f, groupSpace, barSpace)

        chart.xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Trichy", "Coimbatore", "Chennai"))
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setCenterAxisLabels(true)
        chart.xAxis.granularity = 1f
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.invalidate()
    }

    private fun setupChartAvgBmi() {
        val chart = findViewById<LineChart>(R.id.chartAvgBmi)
        val entries = listOf(Entry(0f, 21.6f), Entry(1f, 22.1f), Entry(2f, 21.3f))

        val dataSet = LineDataSet(entries, "Average BMI")
        dataSet.color = Color.parseColor("#2196F3")
        dataSet.setCircleColor(Color.parseColor("#2196F3"))
        dataSet.lineWidth = 2f

        chart.data = LineData(dataSet)
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Trichy", "Coimb.", "Chennai"))
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = false
        chart.invalidate()
    }

    private fun setupChartUnderOver() {
        val chart = findViewById<PieChart>(R.id.chartUnderOver)
        val entries = listOf(PieEntry(62f, "Underweight"), PieEntry(38f, "Overweight"))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(Color.parseColor("#2196F3"), Color.parseColor("#4CAF50"))
        dataSet.valueTextColor = Color.WHITE

        chart.data = PieData(dataSet)
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.invalidate()
    }

    private fun setupChartHeightWeight() {
        val chart = findViewById<BarChart>(R.id.chartHeightWeight)

        val height = BarDataSet(listOf(BarEntry(0f, 124f), BarEntry(1f, 126f), BarEntry(2f, 123f)), "Avg Height")
        height.color = Color.parseColor("#2196F3")

        val weight = BarDataSet(listOf(BarEntry(0f, 27.8f), BarEntry(1f, 28.6f), BarEntry(2f, 26.9f)), "Avg Weight")
        weight.color = Color.parseColor("#4CAF50")

        val data = BarData(height, weight)
        chart.data = data

        // Magic Math to group 2 bars together
        val groupSpace = 0.3f
        val barSpace = 0.05f
        val barWidth = 0.3f
        data.barWidth = barWidth

        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = 3f
        chart.groupBars(0f, groupSpace, barSpace)

        chart.xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Trichy", "Coimbatore", "Chennai"))
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setCenterAxisLabels(true)
        chart.xAxis.granularity = 1f
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.invalidate()
    }

    private fun setupChartBmiDist() {
        val chart = findViewById<PieChart>(R.id.chartBmiDist)
        val entries = listOf(PieEntry(25f, "Obese"), PieEntry(47f, "Overweight"), PieEntry(28f, "Normal"))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(Color.parseColor("#F44336"), Color.parseColor("#FF9800"), Color.parseColor("#4CAF50"))
        dataSet.valueTextColor = Color.WHITE

        chart.data = PieData(dataSet)
        chart.isDrawHoleEnabled = true // Makes it a Donut Chart!
        chart.holeRadius = 40f
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.invalidate()
    }

    private fun setupChartHealthTrend() {
        val chart = findViewById<LineChart>(R.id.chartHealthTrend)

        val healthy = LineDataSet(listOf(Entry(0f, 62f), Entry(1f, 68f), Entry(2f, 71f)), "Healthy %")
        healthy.color = Color.parseColor("#4CAF50")
        healthy.setCircleColor(Color.parseColor("#4CAF50"))

        val atRisk = LineDataSet(listOf(Entry(0f, 38f), Entry(1f, 32f), Entry(2f, 29f)), "At Risk %")
        atRisk.color = Color.parseColor("#F44336")
        atRisk.setCircleColor(Color.parseColor("#F44336"))

        chart.data = LineData(healthy, atRisk)
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Apr", "May", "Jun"))
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.granularity = 1f
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        chart.invalidate()
    }
}