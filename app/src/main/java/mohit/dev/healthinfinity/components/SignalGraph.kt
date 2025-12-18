package mohit.dev.healthinfinity.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun SignalGraph(
    points: List<Int>,
    showMovingAverage: Boolean,
    modifier: Modifier = Modifier
) {
    // Background container for padding and background color
    Box(
        modifier = modifier
            .background(Color(0xFF101010)) // dark background
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (points.size < 2) return@Canvas

            val maxValue = 100f
            val padding = 8.dp.toPx() // small padding inside canvas
            val graphWidth = size.width - 2 * padding
            val graphHeight = size.height - 2 * padding
            val stepX = graphWidth / (points.size - 1)

            // Draw horizontal grid lines
            val horizontalLines = 5
            for (i in 0..horizontalLines) {
                val y = padding + i * (graphHeight / horizontalLines)
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(padding, y),
                    end = Offset(size.width - padding, y),
                    strokeWidth = 1f
                )
            }

            // Draw vertical grid lines (optional for pattern)
            val verticalLines = 5
            for (i in 0..verticalLines) {
                val x = padding + i * (graphWidth / verticalLines)
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(x, padding),
                    end = Offset(x, size.height - padding),
                    strokeWidth = 1f
                )
            }

            // Draw raw signal path (smooth scrolling)
            val path = Path()
            points.forEachIndexed { index, value ->
                val x = padding + index * stepX
                val y = padding + graphHeight - (value / maxValue) * graphHeight
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path = path, color = Color.Green, style = Stroke(width = 3f))

            // Draw moving average line if enabled
            if (showMovingAverage && points.size >= 10) {
                val avgPath = Path()
                val window = 10
                points.forEachIndexed { index, _ ->
                    if (index >= window - 1) {
                        val avg = points.subList(index - window + 1, index + 1).average()
                        val x = padding + index * stepX
                        val y = padding + graphHeight - (avg.toFloat() / maxValue) * graphHeight
                        if (index == window - 1) avgPath.moveTo(x, y) else avgPath.lineTo(x, y)
                    }
                }
                drawPath(path = avgPath, color = Color.Red, style = Stroke(width = 3f))
            }
        }
    }
}
