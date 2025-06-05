package com.chess.candidate.battlequeens

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class GameTimerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val contextMock: Context = mock(Context::class.java)

    private lateinit var viewModel: GameTimerViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        viewModel = GameTimerViewModel()
    }

    @Test
    fun testTimerFunctionality() {
        // Test initial state
        assert(viewModel.timer.value == 0L)
        assert(!viewModel.isRunning())

        // Start the timer
        viewModel.startTimer()
        assert(viewModel.isRunning())

        // Wait for a second and check the timer value
        Thread.sleep(1100) // Sleep for a bit more than 1 second
        assert(viewModel.timer.value > 0)

        // Pause the timer
        viewModel.pauseTimer()
        val pausedValue = viewModel.timer.value

        // Wait for a bit and check that the timer does not change
        Thread.sleep(1000)
        assert(viewModel.timer.value == pausedValue)

        // Reset the timer
        viewModel.resetTimer()
        assert(viewModel.timer.value == 0L)

        // Stop the timer
        viewModel.stopTimer()
        assert(!viewModel.isRunning())
    }
}