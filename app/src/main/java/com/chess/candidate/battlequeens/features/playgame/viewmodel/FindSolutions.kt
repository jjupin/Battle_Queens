package com.chess.candidate.battlequeens.features.playgame.viewmodel

import android.util.Log

/**
 * FindSolutions.kt
 *
 * This file contains the logic for finding solutions to the N-Queens problem.
 * It includes functions to generate all possible solutions, and check if a placement is safe,
 * as well as functions to find the next suggested square based on the current board state.
 * It also includes logic to reset the solutions and manage the latest solution and suggested square.
 *
 * The N-Queens problem is a classic problem in computer science and artificial intelligence,
 * where the goal is to place N queens on an N x N chessboard such that no two queens threaten each other.
 */
object FindSolutions {

    const val TAG = "FindSolutions"

    var nQueenSolutions: ArrayList<ArrayList<Int>> = ArrayList()
    var latestSolution: IntArray? = null
    var latestSuggestedSquare = Pair<Int, Int>(0, 0)


    suspend fun resetSolution(doClearSolutions: Boolean = false, numQueens: Int = 0) {
        if (doClearSolutions) {
            nQueenSolutions.clear()
            nQueenSolutions = nQueen(numQueens)
        }
        latestSolution = null
        latestSuggestedSquare = Pair(0, 0)
    }
    // Function to check if the current placement is safe
    fun isSafe(row: Int, col: Int, rows: Int, ld: Int, rd: Int, n: Int): Boolean {
        return (((rows shr row) and 1) != 1) && (((ld shr (row + col)) and 1) != 1) && (((rd shr (row - col + n)) and 1) != 1)
    }

    // Recursive function to generate all possible permutations
    fun nQueenUtil(
        col: Int, n: Int, board: ArrayList<Int>,
        res: ArrayList<ArrayList<Int>>, rows: Int, ld: Int, rd: Int
    ) {
        // If all queens are placed, add into res
        if (col > n) {
            res.add(ArrayList(board))
            return
        }

        // Try placing a queen in each row
        // of the current column
        for (row in 1..n) {
            // Check if it's safe to place the queen

            if (isSafe(row, col, rows, ld, rd, n)) {
                // Place the queen
                board.add(row)

                // Recur to place the next queen
                nQueenUtil(
                    col + 1, n, board, res,
                    rows or (1 shl row), (ld or (1 shl (row + col))),
                    (rd or (1 shl (row - col + n)))
                )

                // Backtrack: remove the queen
                board.removeAt(board.size - 1)
            }
        }
    }

    fun nQueen(n: Int): ArrayList<ArrayList<Int>> {
        val res = ArrayList<ArrayList<Int>>()

        // Current board configuration
        val board = ArrayList<Int>()

        // Start solving from the first column
        nQueenUtil(1, n, board, res, 0, 0, 0)
        return res
    }

    fun getNextSquare(viewModel: PlayGameViewModel) {
        val n = viewModel.numQueens
        if (nQueenSolutions.size == 0) {
            nQueenSolutions = nQueen(n)
        }

        val input = viewModel.convertQueensOnBoardToIntArray()
        val solution = if (!isSolutionForInput(input) || latestSolution == null) {
            findClosestArray(nQueenSolutions, input)
        } else {
            latestSolution
        }

        if (solution != null) {
            latestSolution = solution
            for (i in solution.indices) {
                if (input[i] == 0) {
                    viewModel.clearSuggestedSquare(latestSuggestedSquare.first, latestSuggestedSquare.second)
                    val row = i
                    val col = solution[i] - 1
                    latestSuggestedSquare = Pair(row, col)
                    viewModel.showNextMove(row, col)
                    return
                }
            }
        } else {
            viewModel.showNoSuggestionsAvailable()
            Log.d(TAG, "No valid solution found for the current board configuration.")
        }
    }

    fun findClosestArray(arrays: ArrayList<ArrayList<Int>> , input: IntArray): IntArray? {
        if (arrays.isEmpty()) {
            return null
        }

        var closestArray: java.util.ArrayList<Int> = arrays[0]
        var minDistance = calculateDistance(input, closestArray.toIntArray())

        for (i in 1 until arrays.size) {
            val currentArray = arrays[i]
            val currentDistance = calculateDistance(input, currentArray.toIntArray())

            if (currentDistance < minDistance) {
                minDistance = currentDistance
                closestArray = currentArray
            }
        }

        return when (closestArrayIsValid(closestArray.toIntArray(), input)) {
            true -> closestArray.toIntArray()
            false -> null
        }
    }

    fun closestArrayIsValid(solution: IntArray, input: IntArray): Boolean {
        if (solution.size != input.size) {
            return false
        }
        for (i in solution.indices) {
            if (input[i] != 0 && input[i] != solution[i]) {
                return false
            }
        }
        return true
    }

    fun calculateDistance(arr1: IntArray, arr2: IntArray): Int {
        if (arr1.size != arr2.size) {
            return Int.MAX_VALUE //Arrays of different sizes can't be compared meaningfully
        }
        var distance = 0
        for (i in arr1.indices) {
            distance += Math.abs(arr1[i] - arr2[i])
        }
        return distance
    }

    //
    // isSolutionForInput - checks that the user's input matches the latest selected
    // solution.  When choosing the next square, if the user does not select the
    // suggested square from Einstein, then the latest solution is invalidated - so
    // must search again for another solution that matches the placed queens.
    //

    fun isSolutionForInput(input: IntArray): Boolean {
        if (latestSolution == null) {
            return false
        }
        if (input.size != latestSolution!!.size) {
            return false
        }
        for (i in input.indices) {
            if (input[i] != 0) {
                if (input[i] != latestSolution!![i]) {
                    return false
                }
            }
        }
        return true
    }
}