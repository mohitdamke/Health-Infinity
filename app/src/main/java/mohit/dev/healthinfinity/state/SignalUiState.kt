package mohit.dev.healthinfinity.state

data class SignalUiState(
    val points: List<Int> = emptyList(),
    val movingAverage: List<Float> = emptyList(),
    val showMovingAverage: Boolean = false
)
