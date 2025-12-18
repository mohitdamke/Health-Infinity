package mohit.dev.healthinfinity.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mohit.dev.healthinfinity.data.SignalDataSource
import mohit.dev.healthinfinity.state.SignalUiState

class SignalViewModel(
    private val dataSource: SignalDataSource = SignalDataSource()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignalUiState())
    val uiState: StateFlow<SignalUiState> = _uiState

    init {
        startCollectingSignal()
    }

    private fun startCollectingSignal() {
        viewModelScope.launch(Dispatchers.Default) {
            dataSource.signalFlow().collect { value ->
                updateSignal(value)
            }
        }
    }

    private fun updateSignal(value: Int) {
        val updatedPoints = (_uiState.value.points + value)
            .takeLast(300) // 30s window (100ms * 300)
        Log.d("SignalViewModel", "New point added: $value, total points: ${updatedPoints.size}")

        _uiState.value = _uiState.value.copy(
            points = updatedPoints
        )
    }

    fun toggleMovingAverage() {
        _uiState.value = _uiState.value.copy(
            showMovingAverage = !_uiState.value.showMovingAverage
        )
    }
}
