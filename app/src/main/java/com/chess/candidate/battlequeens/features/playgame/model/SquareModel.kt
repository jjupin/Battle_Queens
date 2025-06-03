package com.chess.candidate.battlequeens.features.playgame.model

val rowMapper = mapOf(
    0 to "A",
    1 to "B",
    2 to "C",
    3 to "D",
    4 to "E",
    5 to "F",
    6 to "G",
    7 to "H",
    8 to "I",
    9 to "J",
    10 to "K",
    11 to "L",
    12 to "M",
    13 to "N",
    14 to "O",
    15 to "P",
)

data class SquareModel(
    val row: Int,
    val column: Int,
    val hasQueen: Boolean = false,
    var isHighlighted: Boolean = false,
    var showPowerLines: Boolean = false,
    var isKilled: Boolean = false,
    var isStartKill: Boolean = false,  // used to indicate the square is the one who killed another queen
    var isInQueensPath: Boolean = false,
    var isNextMove: Boolean = false
) {
    override fun toString(): String {
        return "Square(row=$row, column=$column, hasQueen=$hasQueen, isHighlighted=$isHighlighted, showPowerLines=$showPowerLines, isInQueensPath=$isInQueensPath, isKilled=$isKilled, isStartKill=$isStartKill)"
    }
}