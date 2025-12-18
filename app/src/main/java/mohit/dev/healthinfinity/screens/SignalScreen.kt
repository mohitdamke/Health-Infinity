package mohit.dev.healthinfinity.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import mohit.dev.healthinfinity.components.SignalGraph
import mohit.dev.healthinfinity.components.StylishSignalGraph
import mohit.dev.healthinfinity.viewmodel.SignalViewModel

@Composable
fun SignalScreen(
    viewModel: SignalViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()



    StylishSignalGraph(
            points = state.points,
            showMovingAverage = state.showMovingAverage,
            modifier = Modifier.fillMaxSize()
        )
    }



