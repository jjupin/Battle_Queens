package com.chess.candidate.battlequeens.data

data class GameStat(
    val numQueens: Int = 0,
    val timePlayed: Long = 0L,
    val datePlayed: Long = 0L,
    val usedEinstein: Boolean = false,
    val winningBoard: List<Int> = emptyList(),
    val id: String,
) {
    override fun toString(): String {
        return "GameStat(numQueens=$numQueens, timePlayed=$timePlayed, datePlayed=$datePlayed, winningBoard=$winningBoard id='$id')"
    }

    fun toDateString(): String {
        return java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            .format(java.util.Date(datePlayed))
    }
}