package com.chess.candidate.battlequeens.features.playgame.model

import com.chess.candidate.battlequeens.utils.Constants.AppConstants.MINIMUM_NUMBER_QUEENS

data class BoardModel(
    val numQueens: Int = MINIMUM_NUMBER_QUEENS,
    val squares: Array<Array<SquareModel>> = Array(numQueens) { row ->
        Array(numQueens) { col ->
            SquareModel(row, col)
        }
    },
) {

    fun getSquare(row: Int, col: Int): SquareModel {
        return squares[row][col]
    }

    fun addQueen(row: Int, col: Int): SquareModel {
        return squares[row][col].copy(hasQueen = true)
    }

    fun removeQueen(row: Int, col: Int): SquareModel {
        return squares[row][col].copy(hasQueen = false, isHighlighted = false, isKilled = false)
    }

    fun numberOfQueensOnBoard(): Int {
        return squares.sumOf { row -> row.count { it.hasQueen } }
    }

    fun numberOfQueensKilled(): Int {
        return squares.sumOf { row -> row.count { it.isKilled } }
    }

    fun highlightSquare(row: Int, col: Int) {
        if (row in 0 until numQueens && col in 0 until numQueens) {
            squares[row][col].isHighlighted = true
        }
    }

    fun numberOfAvailableSquares(): Int {
        return squares.sumOf { row -> row.count { !it.hasQueen && !it.isInQueensPath} }
    }

    //
    // Available square is defined as a square that is not occupied by a queen and is
    // not in the path of any queen
    //
    fun getAvailableSquare(): SquareModel? {
        for (row in squares) {
            for (square in row) {
                if (!square.hasQueen && !square.isInQueensPath) {
                    return square
                }
            }
        }
        return null
    }

    fun getAllAvailableSquares(): List<SquareModel> {
        val availableSquares = mutableListOf<SquareModel>()
        for (row in squares) {
            for (square in row) {
                if (!square.hasQueen && !square.isInQueensPath) {
                    availableSquares.add(square)
                }
            }
        }
        return availableSquares
    }

}