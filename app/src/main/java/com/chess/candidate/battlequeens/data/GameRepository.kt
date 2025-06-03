package com.chess.candidate.battlequeens.data

import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getGameStatsStream(): Flow<List<GameStat>>

    suspend fun getAllGameStats(forceUpdate: Boolean = false): List<GameStat>

    fun getGameStatsStream(gameStatId: String): Flow<GameStat?>

    suspend fun getGameStat(gameStatId: String): GameStat?

    suspend fun createGameStat(numQueens: Int, timePlayed: Long, datePlayed: Long, usedEinstein: Boolean, winningBoard: List<Int>): String

    suspend fun updateGameStat(gameStatId: String, numQueens: Int, timePlayed: Long)

    suspend fun deleteAllGameStats()

    suspend fun deleteGameStat(gameStatId: String)
}