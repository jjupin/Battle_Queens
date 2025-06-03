package com.chess.candidate.battlequeens.features.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chess.candidate.battlequeens.utils.formatTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameTimerViewModel: ViewModel() {
    private val _timer = MutableStateFlow(0L)
    val timer = _timer.asStateFlow()

    private var timerJob: Job? = null

    fun isRunning(): Boolean {
        return timerJob?.isActive ?: false
    }

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timer.value++
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
    }

    fun resetTimer() {
        _timer.value = 0
        timerJob?.cancel()
    }

    fun stopTimer() {
        _timer.value = 0
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    fun getTimerValueAsString(): String {
        return _timer.value.formatTime()
    }
}