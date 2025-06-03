package com.chess.candidate.battlequeens.data

import com.chess.candidate.battlequeens.data.source.GameStatDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID


class GameRepositoryImpl(
    private val localDataSource: GameStatDao,
    private val dispatcher: CoroutineDispatcher,
) : GameRepository {

    override fun getGameStatsStream(): Flow<List<GameStat>> {
        return localDataSource.observeAll().map { gameStats ->
            withContext(dispatcher) {
                gameStats.toExternal()
            }
        }
    }

    override suspend fun getAllGameStats(forceUpdate: Boolean): List<GameStat> {
        return withContext(dispatcher) {
            localDataSource.getAll().toExternal()
        }
    }


    override fun getGameStatsStream(gameStatId: String): Flow<GameStat?> {
        return localDataSource.observeById(gameStatId).map { it.toExternal() }
    }

    override suspend fun getGameStat(gameStatId: String): GameStat? {
        return localDataSource.getById(gameStatId)?.toExternal()
    }

    override suspend fun createGameStat(numQueens: Int, timePlayed: Long, datePlayed: Long, usedEinstein: Boolean, winningBoard: List<Int>): String {
        val gameStatId = withContext(dispatcher) {
            UUID.randomUUID().toString()
        }
        val gameStat = GameStat(
            numQueens = numQueens,
            timePlayed = timePlayed,
            datePlayed = datePlayed,
            usedEinstein = usedEinstein,
            winningBoard = winningBoard,
            id = gameStatId,
        )
        localDataSource.upsert(gameStat.toLocal())
        return gameStatId
    }

    override suspend fun updateGameStat(gameStatId: String, numQueens: Int, timePlayed: Long) {
        val gameStat = getGameStat(gameStatId)?.copy(
            numQueens = numQueens,
            timePlayed = timePlayed
        ) ?: throw Exception("GameStat (id $gameStatId) not found")

        localDataSource.upsert(gameStat.toLocal())
    }

    override suspend fun deleteAllGameStats() {
        localDataSource.deleteAll()
    }

    override suspend fun deleteGameStat(gameStatId: String) {
        localDataSource.deleteById(gameStatId)
    }
}