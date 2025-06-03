package com.chess.candidate.battlequeens.data.source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GameStatDao {

    /**
     * Select all game stats from the stats table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM stats")
    suspend fun getAll(): List<LocalGameStat>

    /**
     * Select a gameStat by id.
     *
     * @param gameStatId the gameStat id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM stats WHERE id = :gameStatId")
    suspend fun getById(gameStatId: String): LocalGameStat?

    /**
     * Insert or update a gameStat in the database. If a gameStat already exists, replace it.
     *
     * @param gameStat the gameStat to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(task: LocalGameStat)

    /**
     * Insert or update gameStats in the database. If a gameStat already exists, replace it.
     *
     * @param gameStats the gameStats to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(stats: List<LocalGameStat>)

    /**
     * Delete a gameStat by id.
     *
     * @return the number of gameStat deleted. This should always be 1.
     */
    @Query("DELETE FROM stats WHERE id = :gameStatId")
    suspend fun deleteById(gameStatId: String): Int

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM stats")
    suspend fun deleteAll()

    /**
     * Observes list of GameStats.
     *
     * @return all GameStats.
     */
    @Query("SELECT * FROM stats")
    fun observeAll(): Flow<List<LocalGameStat>>

    /**
     * Observes a single GameStat.
     *
     * @param gameStatId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM stats WHERE id = :gameStatId")
    fun observeById(gameStatId: String): Flow<LocalGameStat>

}