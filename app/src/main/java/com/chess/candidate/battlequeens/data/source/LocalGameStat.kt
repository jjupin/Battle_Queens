package com.chess.candidate.battlequeens.data.source

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "stats"
)
data class LocalGameStat(
    @PrimaryKey val id: String,
    var numQueens: Int,
    var time: Long,
    var date: Long,
    var usedEinstein: Boolean = false,
    var winningBoard: List<Int> = emptyList()
)