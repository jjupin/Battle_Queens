package com.chess.candidate.battlequeens

import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import com.chess.candidate.battlequeens.features.playgame.BoardManager
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesViewModel
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import com.chess.candidate.battlequeens.features.playgame.model.SquareModel
import com.chess.candidate.battlequeens.features.playgame.viewmodel.FindSolutions
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel.GameState
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PlayGameViewModelTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var viewModel: PlayGameViewModel

    private lateinit var boardManager: BoardManager
    private lateinit var timerViewModel: GameTimerViewModel
    private lateinit var prefsViewModel: UserPreferencesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        boardManager = BoardManager()
        timerViewModel = GameTimerViewModel()
        prefsViewModel = UserPreferencesViewModel(
            UserPreferencesRepository(appContext))

        viewModel = PlayGameViewModel(
            appContext = appContext,
            boardManager = boardManager,
            timerViewModel = timerViewModel,
            prefsViewModel = prefsViewModel
        )
    }

    @Test
    fun startGameCorrectly(): Unit = runTest {
        // Arrange
        val initialState = PlayGameViewModel.GameState.START
        val NUM_QUEENS = 8  // user enters 8 queens
        viewModel.setNumberOfQueens(NUM_QUEENS)

        assertEquals(NUM_QUEENS, viewModel.numQueens)
        assertEquals(initialState, viewModel.gameState.value)

        val newState = PlayGameViewModel.GameState.PLAYING
        viewModel.updateGameState(GameState.PLAYING) // when setting number of queens, the code then advances the playing state
        assertEquals(newState, viewModel.gameState.value)
    }

    @Test
    fun checkValidQueenAddition() = runTest {
        val sqrModel = SquareModel(1, 1)
        viewModel.addQueen(sqrModel)  // queen is added to the board
        val currentState = PlayGameViewModel.GameState.PLAYING

        // Assert
        assertEquals(1, viewModel.boardManager.getQueensOnBoardCount())

        // now check that the queen has not been killed...
        val square = viewModel.getSquare(1,1)
        assertEquals(false, square.isKilled)
    }

    @Test
    fun addNextQueenThatKillsAnotherQueen() = runTest {
        // Arrange
        val sqrModel1 = SquareModel(1, 1)
        val sqrModel2 = SquareModel(1, 3) // this will kill the queen at (1, 1)

        viewModel.addQueen(sqrModel1)  // add first queen
        assertEquals(1, viewModel.boardManager.getQueensOnBoardCount())

        // Act
        viewModel.addQueen(sqrModel2)  // add second queen that kills the first
        assertEquals(2, viewModel.boardManager.getQueensOnBoardCount())

        // Assert one is dead
        assertTrue(viewModel.getSquare(1, 3).isKilled)
    }

    @Test
    fun solveTheGame() = runTest {
        val squaresToTap = listOf(
        SquareModel(0, 1),
        SquareModel(2, 0),
        SquareModel(3, 2),
        SquareModel(1, 3)
        )

        squaresToTap.forEach(viewModel::onTap)

        // Assert
        assertEquals(4, viewModel.boardManager.getQueensOnBoardCount())

        // at this point, there has been four queens added - so, if no kills, then game should be won
        assertEquals(GameState.GAME_WON, viewModel.gameState.value)
    }

    @Test
    fun validateBlockedBoard() = runTest {
        // Arrange
        val blockedSquares = listOf(
            SquareModel(0, 1),
            SquareModel(2, 0),
            SquareModel(3, 3)
        )

        // Act
        blockedSquares.forEach { viewModel.onTap(it) }

        // Assert
        assertEquals(3, viewModel.boardManager.getQueensOnBoardCount())
        assertEquals(GameState.GAME_BLOCKED, viewModel.gameState.value)
    }

    @Test
    fun validateRemoveLastQueenFromBlockedBoard() = runTest {
        // Arrange
        val blockedSquares = listOf(
            SquareModel(0, 1),
            SquareModel(2, 0),
            SquareModel(3, 3)
        )

        // Act
        blockedSquares.forEach { viewModel.onTap(it) }

        // Assert
        assertEquals(3, viewModel.boardManager.getQueensOnBoardCount())
        assertEquals(GameState.GAME_BLOCKED, viewModel.gameState.value)

        // Now remove the last queen
        // and show the board is no longer blocked...
        viewModel.removeLastQueen()
        assertEquals(2, viewModel.boardManager.getQueensOnBoardCount())
        assertEquals(GameState.PLAYING, viewModel.gameState.value)
    }

    @Test
    fun resetGameFromBlockedState() = runTest {
        // Arrange
        val blockedSquares = listOf(
            SquareModel(0, 1),
            SquareModel(2, 0),
            SquareModel(3, 3)
        )

        // Act
        blockedSquares.forEach { viewModel.onTap(it) }

        // Assert
        assertEquals(3, viewModel.boardManager.getQueensOnBoardCount())
        assertEquals(GameState.GAME_BLOCKED, viewModel.gameState.value)

        // Now reset the game
        viewModel.resetGame()
        viewModel.updateGameState(GameState.PLAYING)

        // Assert the game is reset
        assertEquals(0, viewModel.boardManager.getQueensOnBoardCount())
        assertEquals(GameState.PLAYING, viewModel.gameState.value)
    }

    @Test
    fun predictGameOutcome() = runTest {
        // setup an 8x8 board with no queens placed
        val initialState = PlayGameViewModel.GameState.START
        val NUM_QUEENS = 8  // user enters 8 queens
        viewModel.setNumberOfQueens(NUM_QUEENS)

        assertEquals(NUM_QUEENS, viewModel.numQueens)
        assertEquals(initialState, viewModel.gameState.value)

        val newState = PlayGameViewModel.GameState.PLAYING
        viewModel.updateGameState(PlayGameViewModel.GameState.PLAYING) // when setting number of queens, the code then advances the playing state
        assertEquals(newState, viewModel.gameState.value)

        // now place three queens guaranteed to lead to a blocked game...
        val squaresToTap = listOf(
            SquareModel(0, 1),
            SquareModel(2, 0),
            SquareModel(1, 7)
        )

        // Act
        squaresToTap.forEach(viewModel::onTap)

        // Assert
        assertEquals(3, viewModel.boardManager.getQueensOnBoardCount())

        // now simulate the Einstein button being tapped.
        // This should predict the next square to tap and update the game state accordingly.
        viewModel.einsteinButtonTapped()
        FindSolutions.getNextSquare(viewModel) // no solution available leads to NO_SOLUTIONS_AVAILABLE state

        // Assert that the game now has no path to a solution
        assertEquals(GameState.NO_SOLUTIONS_AVAILABLE, viewModel.gameState.value)

    }
}