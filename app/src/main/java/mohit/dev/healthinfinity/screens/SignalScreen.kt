package mohit.dev.healthinfinity.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mohit.dev.healthinfinity.components.StylishSignalGraph
import mohit.dev.healthinfinity.viewmodel.SignalViewModel

@Composable
fun SignalScreen(
    viewModel: SignalViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Gradient background for the whole screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                )
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Real-Time Signal Visualizer",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF0D47A1)
                )
                Text(
                    text = "Monitor your live data stream smoothly",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1976D2)
                )
            }

            // Switch Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show Moving Average",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF0D47A1)
                )
                Switch(
                    checked = state.showMovingAverage,
                    onCheckedChange = { viewModel.toggleMovingAverage() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF1976D2),
                        checkedTrackColor = Color(0xFFBBDEFB)
                    )
                )
            }

            // Graph container with rounded card
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                StylishSignalGraph(
                    points = state.points,
                    movingAverage = state.movingAverage,
                    showMovingAverage = state.showMovingAverage,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
