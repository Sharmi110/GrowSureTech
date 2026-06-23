package com.example.growsuretech // Keep your exact package!

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class ParentAnalyticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#0A58CA")
        setContentView(R.layout.activity_parent_analytics)

        // Make the Back arrow work!
        val btnBack = findViewById<TextView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // This closes the screen and goes back to the dashboard
        }

        setupLineChart()
    }

    private fun setupLineChart() {
        val lineChart = findViewById<LineChart>(R.id.parentLineChart)

        // Dummy Data: Child's weight over the last 5 months
        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, 22.1f)) // Month 1
        entries.add(Entry(1f, 23.5f)) // Month 2
        entries.add(Entry(2f, 24.8f)) // Month 3
        entries.add(Entry(3f, 26.2f)) // Month 4
        entries.add(Entry(4f, 28.0f)) // Month 5 (Current)

        val dataSet = LineDataSet(entries, "Weight (kg)")
        dataSet.color = Color.parseColor("#4CAF50")
        dataSet.lineWidth = 3f
        dataSet.circleRadius = 5f
        dataSet.setCircleColor(Color.parseColor("#2E7D32"))
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK

        lineChart.data = LineData(dataSet)

        val months = arrayOf("Feb", "Mar", "Apr", "May", "Jun")
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(months)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.granularity = 1f
        lineChart.xAxis.setDrawGridLines(false)

        lineChart.description.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.animateX(1000)
        lineChart.invalidate()
    }
}