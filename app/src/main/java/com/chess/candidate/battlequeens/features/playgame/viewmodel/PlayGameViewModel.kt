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

    enum class GameState {
        START,
        PLAYING,
        GAME_OVER,
        GAME_WON,
        GAME_BLOCKED, // when the user cannot complete due to no available squares without dying...
        NO_SOLUTIONS_AVAILABLE,
    }

    private val _boardManager = boardManager

    private val _gameState = MutableStateFlow<GameState>(GameState.START)
    val gameState: StateFlow<GameState> = _gameState

    var _showKilledQueensPaths = mutableStateOf(true)  // will be set in settings...

    var _numQueens = mutableStateOf(AppConstants.MINIMUM_NUMBER_QUEENS)
    val numQueens: Int
        get() = _numQueens.value

    var _lastAddedQueenSquare = mutableStateOf(SquareModel(0, 0))

    private var squaresWithQueen = mutableListOf<SquareModel>()
    private var _usedEinstein = mutableStateOf(false) // changes when user clicks on the Einstein button

    val boardState: StateFlow<BoardModel> = _boardManager.boardState

    private var _highlightedSquares = mutableStateOf<List<SquareModel>>(emptyList())
    val highlightedSquares: List<SquareModel>
        get() = _highlightedSquares.value

    private val _highLightCounter = MutableStateFlow(0)
    val highLightCounter = _highLightCounter.asStateFlow()

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

    fun updateFirstTime(firstTime: Boolean) {
        viewModelScope.launch {
            val currentPrefs = prefsViewModel.userPrefs.value ?: UserPreferences()
            val newPrefs = currentPrefs.copy(isFirstTime = firstTime)
            prefsViewModel.updateUserPreferences(newPrefs)
        }
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
        _gameState.value = GameState.PLAYING
    }

    fun showNextMove(row: Int, col: Int) {
        // Show the next move for the user - this is used when the user
        // taps on the Einstein button to get a suggestion for the next move.
        _boardManager.highlightNextMove(row, col)
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
            _usedEinstein.value = false
            FindSolutions.resetSolution()
        }
        _boardManager.resetGame()
    }

    // Handle actions from the board - originally, I was going to show the power lines of the
    // tapped queen - the lanes where another queen could be placed without being attacked.
    // However, I decided to remove this feature for now.
    override fun onDoubleTap(whichSquare: SquareModel): SquareModel {
        // Handle double tap action
        //whichSquare.showPowerLines = !whichSquare.showPowerLines
        //highlightQueensPowerPaths(whichSquare)

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
                //_usedEinstein.value = newPrefs.isEinsteinModeEnabled
            }
        }
    }

    fun einsteinButtonTapped() {
        _usedEinstein.value = true
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
                isEinsteinModeEnabled = prefs.isEinsteinModeEnabled,
                wallpaper = prefs.wallpaper,
                isFirstTime = prefs.isFirstTime
            )
            prefsViewModel.updateUserPreferences(newPrefs)
        }
    }

    fun getUserPrefsAsStream() : StateFlow<UserPreferences?> {
        return prefsViewModel.userPrefs
    }

    fun getPulledFromDatastoreAsStream(): StateFlow<Boolean> {
        return prefsViewModel.pulledFromDatastore
    }

}