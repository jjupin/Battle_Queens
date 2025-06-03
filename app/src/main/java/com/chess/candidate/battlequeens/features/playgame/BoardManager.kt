package com.chess.candidate.battlequeens.features.playgame

import com.chess.candidate.battlequeens.features.playgame.model.BoardModel
import com.chess.candidate.battlequeens.features.playgame.model.SquareModel
import com.chess.candidate.battlequeens.utils.Constants.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BoardManager {
    private var _board = BoardModel(
        numQueens = AppConstants.MINIMUM_NUMBER_QUEENS,
        squares = Array(AppConstants.MINIMUM_NUMBER_QUEENS) { row ->
            Array(AppConstants.MINIMUM_NUMBER_QUEENS) { col ->
                SquareModel(row, col)
            }
        }
    )

    val board: BoardModel
        get() = _board

    val _boardState = MutableStateFlow(_board)
    val boardState: StateFlow<BoardModel> = _boardState

    fun getSquare(row: Int, col: Int): SquareModel {
        val model = _boardState.value
        val square = model.getSquare(row, col)
        return square //_boardState.value.getSquare(row, col)
    }

    fun getBoardSize(): Int {
        return _board.numQueens
    }

    fun getWinningBoard(): List<Int> {
        val winningBoard = mutableListOf<Int>()
        for (row in 0 until _board.numQueens) {
            for (col in 0 until _board.numQueens) {
                val square = _board.getSquare(row, col)
                winningBoard.add(if (square.hasQueen) 1 else 0)
            }
        }
        return winningBoard
    }

    fun getBoardSquaresCount() = _board.numQueens * _board.numQueens

    fun updateBoardWithQueenCount(numQueens: Int) {
        val squares = Array(numQueens) { row ->
            Array(numQueens) { col ->
                SquareModel(row, col)
            }
        }
        updateManagersBoard(numQueens, squares)
    }

    fun updateBoardWithNewSquares(squares: Array<Array<SquareModel>>): BoardModel {
        return updateManagersBoard(
            queenCount = _board.numQueens,
            boardSquares = squares
        )
    }

    fun updateManagersBoard(queenCount: Int, boardSquares: Array<Array<SquareModel>>): BoardModel {
        _board = BoardModel(
            numQueens = queenCount,
            squares = boardSquares
        )
        _boardState.value = _board

        return _board
    }

    fun resetGame() { // resets it back to an initial state with the num queens selected
        updateBoardWithQueenCount(_board.numQueens)
        _boardState.value = _board
    }

    fun addQueen(row: Int, col: Int) {
        adjustQueensInModel(row = row, col = col, doInsert = true)
    }

    fun removeQueen(row: Int, col: Int) {
        adjustQueensInModel(row, col, doInsert = false)
    }

    fun highlightNextMove(row: Int, col:Int) {
        val newBoardSquares = _board.squares.copyOf()
        newBoardSquares[row][col] =
            newBoardSquares[row][col].copy(isNextMove = true)
        updateBoardWithNewSquares(newBoardSquares)
        updateAttackedSquaresForAllQueens()
    }
    fun clearSuggestedSquare(row:Int, col:Int) {
        val newBoardSquares = _board.squares.copyOf()
        newBoardSquares[row][col] =
            newBoardSquares[row][col].copy(isNextMove = false)
        updateBoardWithNewSquares(newBoardSquares)
        updateAttackedSquaresForAllQueens()
    }

    private fun adjustQueensInModel(row: Int, col: Int, doInsert: Boolean = true) {
        resetHighlightedPaths()
        val newBoardSquares = _board.squares.copyOf()
        val squareCopy = newBoardSquares[row][col].copy(hasQueen = doInsert)
        if (!doInsert) { // clear out all the data associated with being killed
            squareCopy.isKilled = false
            squareCopy.isStartKill = false
            squareCopy.isInQueensPath = false
            squareCopy.isHighlighted = false
        }
        newBoardSquares[row][col] = squareCopy
        if (getQueensOnBoardCount() > 1) {
            if (doInsert) {
                canKillQueen(squareCopy, newBoardSquares)
            }
        }
        updateBoardWithNewSquares(newBoardSquares)
        updateAttackedSquaresForAllQueens()
    }

    fun isGameBlocked(): Boolean {
        val attacked = getNumberOfAttackedSquares()
        val totalSquares = getBoardSquaresCount()
        val availSquares = totalSquares - attacked
        val totalQueensNotOnBoard = _board.numQueens - getQueensOnBoardCount()

        return (availSquares == 0) && (totalQueensNotOnBoard > 0)   // all squares attacked - no place to put a queen
    }

    fun getQueensOnBoard(): List<SquareModel> {
        val queensOnBoard = mutableListOf<SquareModel>()
        for (row in 0 until _board.numQueens) {
            for (col in 0 until _board.numQueens) {
                val square = _board.getSquare(row, col)
                if (square != null) {
                    if (square.hasQueen) {
                        queensOnBoard.add(square)
                    }
                }
            }
        }
        return queensOnBoard
    }

    fun getQueensOnBoardCount() = getQueensOnBoard().size
    fun convertQueensOnBoardToIntArray(): IntArray {
        val queensOnBoard = getQueensOnBoard()
        val conversion = IntArray(_board.numQueens) { 0}
        for (i in 0 until _board.numQueens) {
            val queenSquare = queensOnBoard.getOrNull(i)
            queenSquare?.let { queen ->
                conversion[queenSquare.row] = queenSquare.column + 1
            }
        }
        return conversion
    }

    fun getKilledQueensCount(): Int {
        return _board.squares.sumOf { row -> row.count { it.isKilled } }
    }

    fun canKillQueen(whichSquare: SquareModel, squaresCopy: Array<Array<SquareModel>>): Boolean {
        var canKillQueen = false
        for (row in 0 until _board.numQueens) {
            for (col in 0 until _board.numQueens) {
                val square = squaresCopy[row][col]
                if (square != null && square.hasQueen && !sameSquare(square, whichSquare)) {
                    val canKill = canKillQueen(square, whichSquare)
                    whichSquare.isKilled = canKill
                    square.isStartKill = canKill
                    squaresCopy[whichSquare.row][whichSquare.column] =
                        whichSquare.copy(isKilled = canKill)
                    highlightPath(square, whichSquare, squaresCopy, canKill)
                    if (canKill) {
                        return true
                    }
                }
            }
        }
        return canKillQueen
    }

    fun canKillQueen(startQueen: SquareModel, endQueen: SquareModel): Boolean {
        return (startQueen.row == endQueen.row ||
                startQueen.column == endQueen.column ||
                Math.abs(startQueen.row - endQueen.row) == Math.abs(startQueen.column - endQueen.column))
    }

    fun sameSquare(square1: SquareModel, square2: SquareModel): Boolean {
        return (square1.row == square2.row && square1.column == square2.column)
    }

    // highlighting paths between queens
    fun highlightPath(
        from: SquareModel,
        to: SquareModel,
        squaresCopy: Array<Array<SquareModel>>,
        isHighlighted: Boolean
    ) {

        // highlight (turn on/off) the starting and ending squares
        squaresCopy[from.row][from.column] =
            squaresCopy[from.row][from.column].copy(isHighlighted = isHighlighted)
        squaresCopy[to.row][to.column] =
            squaresCopy[to.row][to.column].copy(isHighlighted = isHighlighted)

        // Now, determine the inbetween squares along the path...

        val rowDiff = to.row - from.row
        val colDiff = to.column - from.column

        val rowStep = if (rowDiff == 0) 0 else rowDiff / Math.abs(rowDiff)
        val colStep = if (colDiff == 0) 0 else colDiff / Math.abs(colDiff)

        var currentRow = from.row
        var currentCol = from.column

        while (currentRow != to.row || currentCol != to.column) {
            squaresCopy[currentRow][currentCol] =
                squaresCopy[currentRow][currentCol].copy(isHighlighted = isHighlighted)
            currentRow += rowStep
            currentCol += colStep
            if (currentRow == _board.numQueens || currentCol == _board.numQueens) break
            if (currentRow < 0 || currentCol < 0) break
        }
    }

    fun resetHighlightedPaths() {
        val updatedSquares = _board.squares.copyOf()
        for (row in 0 until _board.numQueens) {
            for (col in 0 until _board.numQueens) {
                updatedSquares[row][col] =
                    updatedSquares[row][col].copy(
                        isHighlighted = false,
                        showPowerLines = false,
                        isKilled = false,
                        isInQueensPath = false,
                        isStartKill = false
                    )
            }
        }
        updateBoardWithNewSquares(updatedSquares)
    }


    fun updateAttackedSquaresForAllQueens() {
        val queens: List<SquareModel> = getQueensOnBoard()
        for (queen in queens) {
            updateAttackedSquares(forQueen = queen)
        }
    }

    fun updateAttackedSquares(forQueen: SquareModel) {
        val row = forQueen.row
        val col = forQueen.column
        val updatedSquares = _board.squares.copyOf()

        val attackedSquares = getAttackedSquares(row, col, _board.numQueens)
        for (square in attackedSquares) {
            val attackedRow = square.first
            val attackedCol = square.second

            if (attackedRow in 0 until _board.numQueens && attackedCol in 0 until _board.numQueens) {
                updatedSquares[attackedRow][attackedCol] =
                    updatedSquares[attackedRow][attackedCol].copy(isInQueensPath = true)
            }
        }
        updateBoardWithNewSquares(updatedSquares)
    }

    fun getAttackedSquares(row: Int, col: Int, boardSize: Int = 8): List<Pair<Int, Int>> {
        val attackedSquares = mutableListOf<Pair<Int, Int>>()
        attackedSquares.add(Pair(row, col)) // Add the square where the queen is placed

        // Horizontal and vertical attacks
        for (i in 0 until boardSize) {
            if (i != col) attackedSquares.add(Pair(row, i)) // Horizontal
            if (i != row) attackedSquares.add(Pair(i, col)) // Vertical
        }

        // Diagonal attacks
        for (i in -boardSize until boardSize) {
            if (i == 0) continue
            if (row + i in 0 until boardSize && col + i in 0 until boardSize)
                attackedSquares.add(Pair(row + i, col + i)) // Top-left to bottom-right diagonal
            if (row + i in 0 until boardSize && col - i in 0 until boardSize)
                attackedSquares.add(Pair(row + i, col - i)) // Top-right to bottom-left diagonal
        }

        return attackedSquares.distinctBy { it.first to it.second }
    }

    fun getNumberOfAttackedSquares(): Int {
        return _board.squares.sumOf { row -> row.count { it.isInQueensPath } }
    }
}