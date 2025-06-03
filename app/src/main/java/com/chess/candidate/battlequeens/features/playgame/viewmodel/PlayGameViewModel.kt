package com.chess.candidate.battlequeens.features.playgame.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.data.GameRepository
import com.chess.candidate.battlequeens.data.GameRepositoryImpl
import com.chess.candidate.battlequeens.data.GameStat
import com.chess.candidate.battlequeens.data.source.GameDatabase
import com.chess.candidate.battlequeens.features.playgame.BoardManager
import com.chess.candidate.battlequeens.features.playgame.model.BoardModel
import com.chess.candidate.battlequeens.features.playgame.model.SquareModel
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesViewModel
import com.chess.candidate.battlequeens.features.preferences.model.UserPreferences
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import com.chess.candidate.battlequeens.ui.components.board.CurrentData
import com.chess.candidate.battlequeens.ui.components.board.HandleAction
import com.chess.candidate.battlequeens.utils.Constants.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class PlayGameViewModel(appContext: Context, val boardManager: BoardManager,
    val timerViewModel: GameTimerViewModel,
    val prefsViewModel: UserPreferencesViewModel) : ViewModel(), HandleAction, CurrentData {
    val TAG = "PlayGameViewModel"

   // private var timerViewModel = GameTimerViewModel()
    //private var prefsViewModel = UserPreferencesViewModel(UserPreferencesRepository(appContext))

    enum class GameState {
        START,
        PLAYING,
        GAME_OVER,
        GAME_WON,
        GAME_LOST,
        GAME_BLOCKED, // when the user cannot complete due to no available squares without dying...
        NO_SOLUTIONS_AVAILABLE,
        GAME_RESET,
        ERROR
    }

    private val _boardManager = boardManager

    private val _gameState = MutableStateFlow<GameState>(GameState.START)
    val gameState: StateFlow<GameState> = _gameState

    var _showKilledQueensPaths = mutableStateOf(true)  // will be set in settings...
    val showKilledQueensPaths: Boolean
        get() = _showKilledQueensPaths.value

    var _numQueens = mutableStateOf(AppConstants.MINIMUM_NUMBER_QUEENS)
    val numQueens: Int
        get() = _numQueens.value

    var _lastAddedQueenSquare = mutableStateOf(SquareModel(0, 0))

    private var squaresWithQueen = mutableListOf<SquareModel>()
    private var _usedEinstein = mutableStateOf(false) // changes when user uses Einstein mode

    val boardState: StateFlow<BoardModel> = _boardManager.boardState

    private var _highlightedSquares = mutableStateOf<List<SquareModel>>(emptyList())
    val highlightedSquares: List<SquareModel>
        get() = _highlightedSquares.value

    private val _highLightCounter = MutableStateFlow(0)
    val highLightCounter = _highLightCounter.asStateFlow()

    private var _powerLineSquares = mutableStateOf<List<SquareModel>>(emptyList())
    val powerLineSquares: List<SquareModel>
        get() = _powerLineSquares.value

    private val _powerLinesCounter = MutableStateFlow(0)
    val powerLinesCounter = _powerLinesCounter.asStateFlow()

    private val _gameOver = MutableStateFlow(false)
    val gameOver = _gameOver.asStateFlow()

    private val _placedNumQueens = MutableStateFlow(squaresWithQueen.size)
    val placedNumQueens = _placedNumQueens.asStateFlow()

    // room database and repository

    private var db: GameDatabase? = null
    private var repo: GameRepository? = null

    init {
        setNumberOfQueens(AppConstants.MINIMUM_NUMBER_QUEENS)
        db = GameDatabase.getDatabase(appContext, viewModelScope)
        repo = db?.gameStatDao()?.let {
            GameRepositoryImpl(it, Dispatchers.IO)
        } ?: null
    }

    fun updateGameState(newState: GameState) {
        _gameState.value = newState
    }

    fun updateBoardState(squares: Array<Array<SquareModel>>) {
        updateBoardState(
            numQueens = numQueens,
            squares = squares
        )
    }

    fun updateBoardState(numQueens: Int, squares: Array<Array<SquareModel>>) {
        _boardManager.updateManagersBoard(numQueens, squares)
    }

    fun setNumberOfQueens(numQueens: Int) {
        _numQueens.value = numQueens
        viewModelScope.launch {
            FindSolutions.resetSolution(doClearSolutions = true, numQueens)
        }
        _boardManager.updateBoardWithQueenCount(numQueens)
    }

    override fun getSquare(row: Int, col: Int): SquareModel {
        return _boardManager.getSquare(row, col)
    }

    fun getNumberOfQueensOnBoard(): Int {
        return _boardManager.getQueensOnBoardCount()
    }

    fun getNumberOfQueensKilled(): Int {
        return _boardManager.getKilledQueensCount()
    }

    fun convertQueensOnBoardToIntArray(): IntArray {
        return _boardManager.convertQueensOnBoardToIntArray()
    }

    // remove the last queen added to the board
    // this is used when the user wants to remove it after
    // blocking the game - basically recovering from a bad move
    fun removeLastQueenAdded() {
        if (squaresWithQueen.isNotEmpty()) {
            _boardManager.removeQueen(_lastAddedQueenSquare.value.row, _lastAddedQueenSquare.value.column)
        }
    }

    fun getNumberOfAvailableSquares(): Int {
        val attackedSquares = _boardManager.getNumberOfAttackedSquares()
        val remaining = _boardManager.getBoardSquaresCount() - attackedSquares
        return remaining
    }

    fun gameBlocked(): Boolean {
        return _boardManager.isGameBlocked()
    }

    fun addQueen(whichSquare: SquareModel) {
        if (!timerViewModel.isRunning()) {
            timerViewModel.startTimer()
        }

        _boardManager.addQueen(whichSquare.row, whichSquare.column)
        _lastAddedQueenSquare.value = whichSquare
    }

    fun removeQueen(whichSquare: SquareModel) {
        _boardManager.removeQueen(whichSquare.row, whichSquare.column)
    }

    fun removeLastQueen() {
       _boardManager.removeQueen(_lastAddedQueenSquare.value.row, _lastAddedQueenSquare.value.column)
    }

    fun showNextMove(row: Int, col: Int) {
        // Show the next move for the user
        val squares = _boardManager.highlightNextMove(row, col)
    }

    fun showNoSuggestionsAvailable() {
        // Show no suggestions available
        _gameState.value = GameState.NO_SOLUTIONS_AVAILABLE
    }

    fun clearSuggestedSquare(row: Int, col: Int) {
        // Clear the suggested square
        _boardManager.clearSuggestedSquare(row, col)
    }

    fun resetGame() {
        timerViewModel.resetTimer()
        viewModelScope.launch {
            FindSolutions.resetSolution()
        }
        _boardManager.resetGame()
    }

    override fun onDoubleTap(whichSquare: SquareModel): SquareModel {
        // Handle double tap action
        whichSquare.showPowerLines = !whichSquare.showPowerLines
        //highlightQueensPowerPaths(whichSquare)

        if (!whichSquare.showPowerLines) {
            // If the square is not highlighted, remove highlights from all squares
            _powerLineSquares.value = emptyList()
        }

        return whichSquare.copy()
    }

    override fun onTap(whichSquare: SquareModel) {
        // Handle tap action

        if (getNumberOfQueensKilled() > 0 && !(whichSquare.isKilled || whichSquare.isStartKill)) {
            return // don't allow tap if a queen is killed
        }

        when (whichSquare.hasQueen) {
            true -> removeQueen(whichSquare)
            false -> addQueen(whichSquare)
        }

        _gameState.value = GameState.PLAYING

        if (gameBlocked()) {
            _gameState.value = GameState.GAME_BLOCKED
        } else if (getNumberOfQueensOnBoard() == numQueens) {
                if (getNumberOfQueensKilled() == 0) {
                    timerViewModel.pauseTimer()   // game's over, so pause the timer...
                    _gameState.value = GameState.GAME_WON
                }
        }
    }

    fun getKilledQueensCount(): Int {
        return _boardManager.getKilledQueensCount()
    }

    override fun playSound(context: Context, whichSound: Int) {
        // Play sound when a queen is placed
        viewModelScope.launch {
            val mediaPlayer = MediaPlayer.create(context, whichSound)
            mediaPlayer.start()
        }
    }

    fun playQueenKilledSound(context: Context) {
        playSound(context, R.raw.homer_doh)
    }

    fun highlightKilledQueensPaths() {
        _highlightedSquares.value.map {
            it.isHighlighted = false
        }
        _highlightedSquares.value = mutableListOf<SquareModel>()
        _highLightCounter.value = 0
        squaresWithQueen = mutableListOf<SquareModel>()
    }

    fun highlightPathBetweenQueens(
        startSquare: SquareModel,
        endSquare: SquareModel,
        doHighlight: Boolean = true
    ) {
        val highlighted = mutableListOf<SquareModel>()
        startSquare.isHighlighted = doHighlight
        endSquare.isHighlighted = doHighlight

        // now, highlight the path between the two squares

        if (startSquare.row == endSquare.row) {
            // Same row
            for (col in Math.min(
                startSquare.column,
                endSquare.column
            ) until Math.max(startSquare.column, endSquare.column) + 1) {
                val square = getSquare(startSquare.row, col).copy()
                square.isHighlighted = doHighlight
                highlighted.add(square)
            }
        } else if (startSquare.column == endSquare.column) {
            // Same column
            for (row in Math.min(startSquare.row, endSquare.row) until Math.max(
                startSquare.row,
                endSquare.row
            ) + 1) {
                val square = getSquare(row, startSquare.column).copy()
                square.isHighlighted = doHighlight
                highlighted.add(square)
            }
        } else {
            // Diagonal movement
            val rowStep = if (endSquare.row > startSquare.row) 1 else -1
            val colStep = if (endSquare.column > startSquare.column) 1 else -1

            var row = startSquare.row
            var col = startSquare.column

            while (row != endSquare.row && col != endSquare.column) {
                val square = getSquare(row, col).copy()
                square.isHighlighted = doHighlight
                highlighted.add(square)
                row += rowStep
                col += colStep
            }
        }

        _highlightedSquares.value =
            (_highlightedSquares.value.filter { it !in highlighted } + highlighted).distinctBy { it.row to it.column }
        _highLightCounter.value = _highlightedSquares.value.size

        updateBoardWithHighlightedSquares(highlighted, _highlightedSquares.value)
    }

    fun updateBoardWithHighlightedSquares(
        highlighted: List<SquareModel>,
        totalList: List<SquareModel>
    ) {
        viewModelScope.launch {
            /**
            val newBoardSquares = _boardState.value.squares.copyOf()
            for (square in highlighted) {
                val updatedSquare = square.copy(isHighlighted = true)
                newBoardSquares[square.row][square.column] = updatedSquare
            }
            updateBoardState(newBoardSquares)
            //delay(500)

            for (square in totalList) {
                val updatedSquare = square.copy()
                val newBoardSquares = _boardState.value.squares.copyOf()
                newBoardSquares[square.row][square.column] = updatedSquare
                updateBoardState(numQueens, newBoardSquares)
            }
            **/
        }
    }

    /*
    Highlight the paths of the queen based on the tapped square
    */
    fun highlightQueensPowerPaths(whichSquare: SquareModel) {
        // Highlight the paths of the queens based on the tapped square
        val powerLineSquares = mutableListOf<SquareModel>()
        if (!whichSquare.showPowerLines) {
            _powerLineSquares.value.map {
                it.showPowerLines = false
                return
            }
        }

        _powerLineSquares.value = emptyList()

        for (pos in 0 until numQueens) {
            var square = getSquare(pos, whichSquare.column)
            if (square == whichSquare) continue
            square.showPowerLines = !square.showPowerLines
            when (square.showPowerLines) {
                true -> powerLineSquares.add(square)
                false -> powerLineSquares.remove(square)
            }
            square = getSquare(whichSquare.row, pos)
            if (square == whichSquare) continue
            square.showPowerLines = !square.showPowerLines
            when (square.showPowerLines) {
                true -> powerLineSquares.add(square)
                false -> powerLineSquares.remove(square)
            }

            _powerLinesCounter.value = powerLineSquares.size
        }


        _powerLineSquares.value = (_powerLineSquares.value + powerLineSquares).distinct()
        _powerLinesCounter.value = _powerLineSquares.value.size
    }

    //
    // functions to access the timer view model items
    //

    fun getTimeValue(): Long {
        return timerViewModel.timer.value
    }

    fun getTimerValueAsString(): String {
        return timerViewModel.getTimerValueAsString()
    }

    fun getWinningBoard(): List<Int> {
        return _boardManager.getWinningBoard()
    }


    //
    // Statistics functions - for tracking user's games
    //

    val _stats = MutableStateFlow<MutableList<GameStat>>(
        mutableListOf(
            GameStat(id = "1", numQueens = 4, timePlayed = 120),
            GameStat(id = "4", numQueens = 4, timePlayed = 150),
            GameStat(id = "5", numQueens = 4, timePlayed = 3000),
            GameStat(id = "2", numQueens = 5, timePlayed = 157),
            GameStat(id = "3", numQueens = 6, timePlayed = 180)
        )
    )
    val stats: StateFlow<List<GameStat>> = _stats.asStateFlow()

    fun getGameStatsStream(): StateFlow<List<GameStat>> {
        repo?.let {
            return it.getGameStatsStream().stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList<GameStat>()
            )
        } ?: run {
            // Handle the case where the repository is not initialized
            return flowOf(emptyList<GameStat>()).stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList<GameStat>()
            )
        }
    }

    fun saveGameStat(numQueens: Int, timePlayed: Long, datePlayed: Long = System.currentTimeMillis(), winningBoard: List<Int> = emptyList()) {
        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            _stats.value.add(GameStat(id = id, numQueens = numQueens, timePlayed = timePlayed, datePlayed = datePlayed, winningBoard = winningBoard, usedEinstein = _usedEinstein.value))

            repo?.let {
                it.createGameStat(numQueens, timePlayed, datePlayed, _usedEinstein.value, winningBoard)
            } ?: run {
                // Handle the case where the repository is not initialized
                // TO-DO: Handle the error case
            }
        }
    }

    //
    // User Prefs functions
    //

    fun getUserPrefs(): UserPreferences? {
        return prefsViewModel.userPrefs.value
    }

    fun toggleEinsteinMode(modeEnabled: Boolean = false) {
        if (modeEnabled) {
            viewModelScope.launch {
                val currentPrefs = getUserPrefs() ?: UserPreferences()
                val newPrefs =
                    currentPrefs.copy(isEinsteinModeEnabled = modeEnabled)
                prefsViewModel.updateUserPreferences(newPrefs)
                _usedEinstein.value = newPrefs.isEinsteinModeEnabled
            }
        }
    }

    fun updateUserPrefs(prefs: UserPreferences) {
        viewModelScope.launch {
            if (prefs.isEinsteinModeEnabled) {
                _usedEinstein.value = true
            } 
            
            val newPrefs = UserPreferences(
                numQueens = prefs.numQueens,
                isShowAvailableSquaresEnabled = prefs.isShowAvailableSquaresEnabled,
                isSoundEnabled = prefs.isSoundEnabled,
                isDarkMode = prefs.isDarkMode,
                isFastFailEnabled = prefs.isFastFailEnabled,
                isEinsteinModeEnabled = prefs.isEinsteinModeEnabled
            )
            prefsViewModel.updateUserPreferences(newPrefs)
        }
    }

    fun getUserPrefsAsStream() : StateFlow<UserPreferences?> {
        return prefsViewModel.userPrefs
    }

}