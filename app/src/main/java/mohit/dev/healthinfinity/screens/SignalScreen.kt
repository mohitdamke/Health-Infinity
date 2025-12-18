package mohit.dev.healthinfinity.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mohit.dev.healthinfinity.components.StylishSignalGraph
import mohit.dev.healthinfinity.viewmodel.SignalViewModel

@Composable
fun SignalScreen(
    viewModel: SignalViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Show Moving Average")
            Switch(
                checked = state.showMovingAverage,
                onCheckedChange = { viewModel.toggleMovingAverage() }
            )
        }

    StylishSignalGraph(
            points = state.points,
            showMovingAverage = state.showMovingAverage,
            modifier = Modifier.fillMaxSize()
        )
    }

}

