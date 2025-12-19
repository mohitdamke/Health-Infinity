package mohit.dev.healthinfinity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import mohit.dev.healthinfinity.data.SignalDataSource
import mohit.dev.healthinfinity.state.SignalUiState

class SignalViewModel(
    private val dataSource: SignalDataSource = SignalDataSource()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignalUiState())
    val uiState: StateFlow<SignalUiState> = _uiState

    private val buffer = ArrayDeque<Int>(300)
    private val bufferMutex = kotlinx.coroutines.sync.Mutex()



    init {
        startCollectingSignal()
    }

    private fun startCollectingSignal() {
        viewModelScope.launch(Dispatchers.Default) {
            dataSource.signalFlow()
                .collect { value ->
                    addToBuffer(value) // 1000Hz
                }
        }
        startUiTicker()
    }

    private suspend fun addToBuffer(value: Int) {
        bufferMutex.lock()
        try {
            if (buffer.size == 300) {
                buffer.removeFirst()
            }
            buffer.addLast(value)
        } finally {
            bufferMutex.unlock()
        }
    }

    private fun startUiTicker() {
        viewModelScope.launch {
            while (true) {
                emitUiState()
                delay(16)
            }
        }
    }


    private suspend fun emitUiState() {
        val snapshot = bufferMutex.withLock {
            downSample(buffer)
        }

        _uiState.value = _uiState.value.copy(
            points = snapshot,
            movingAverage = if (_uiState.value.showMovingAverage) {
                calculateMovingAverage(snapshot)
            } else emptyList()
        )
    }

    private fun downSample(
        data: Collection<Int>,
        maxPoints: Int = 200
    ): List<Int> {
        if (data.size <= maxPoints) return data.toList()

        val step = data.size / maxPoints
        return data.filterIndexed { index, _ ->
            index % step == 0
        }
    }

    private fun calculateMovingAverage(points: List<Int>, window: Int = 10): List<Float> {
        if (points.size < window) return emptyList()

        val result = mutableListOf<Float>()
        var sum = points.take(window).sum()

        result.add(sum / window.toFloat())

        for (i in window until points.size) {
            sum += points[i]
            sum -= points[i - window]
            result.add(sum / window.toFloat())
        }
        return result
    }

    fun toggleMovingAverage() {
        _uiState.value = _uiState.value.copy(
            showMovingAverage = !_uiState.value.showMovingAverage
        )
    }


}
