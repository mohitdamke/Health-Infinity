package mohit.dev.healthinfinity.data

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class SignalDataSource {

    fun signalFlow(): Flow<Int> = flow {
        while (true) {
            emit(Random.nextInt(0, 101))
            delay(100)
        }
    }

}