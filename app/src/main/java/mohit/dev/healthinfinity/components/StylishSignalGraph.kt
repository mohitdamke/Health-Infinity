package mohit.dev.healthinfinity.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun StylishSignalGraph(
    points: List<Int>,
    showMovingAverage: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (points.size < 2) return@Canvas

            val maxValue = 100f
            val padding = 16.dp.toPx()
            val graphWidth = size.width - 2 * padding
            val graphHeight = size.height - 2 * padding
            val stepX = graphWidth / (points.size - 1)

            // Draw subtle grid
            val horizontalLines = 5
            val gridPaint = Color.LightGray.copy(alpha = 0.3f)
            for (i in 0..horizontalLines) {
                val y = padding + i * (graphHeight / horizontalLines)
                drawLine(gridPaint, Offset(padding, y), Offset(size.width - padding, y), 1f)
            }

            // Draw raw signal with smooth cubic path
            val path = Path()
            points.forEachIndexed { index, value ->
                val x = padding + index * stepX
                val y = padding + graphHeight - (value / maxValue) * graphHeight
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            // Optional: fill under the curve for style
            val fillPath = Path().apply {
                // Copy the original points
                points.forEachIndexed { index, value ->
                    val x = padding + index * stepX
                    val y = padding + graphHeight - (value / maxValue) * graphHeight
                    if (index == 0) moveTo(x, y) else lineTo(x, y)
                }
                // Close the path to bottom
                lineTo(padding + (points.size - 1) * stepX, size.height - padding)
                lineTo(padding, size.height - padding)
                close()
            }
            drawPath(fillPath, brush = Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color.Transparent)))

            // Draw the main line
            drawPath(path, color = Color(0xFF1976D2), style = Stroke(width = 4f, cap = StrokeCap.Round))

            // Draw points on the line
            points.forEachIndexed { index, value ->
                val x = padding + index * stepX
                val y = padding + graphHeight - (value / maxValue) * graphHeight
                drawCircle(Color.White, radius = 6f, center = Offset(x, y))
                drawCircle(Color(0xFF1976D2), radius = 3f, center = Offset(x, y))
            }

            // Moving average
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
                drawPath(avgPath, color = Color.Red, style = Stroke(width = 3f, cap = StrokeCap.Round))
            }
        }
    }
}
