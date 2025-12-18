package mohit.dev.healthinfinity.state

data class SignalUiState(
    val points: List<Int> = emptyList(),
    val showMovingAverage: Boolean = false
)
