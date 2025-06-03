package com.chess.candidate.battlequeens.data.source

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "boards"
)
data class LocalBoard(
    @PrimaryKey val id: String,
    var numQueens: Int,
    var time: Long,
)