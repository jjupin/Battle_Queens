package com.chess.candidate.battlequeens.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope

@Database(entities = [LocalGameStat::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {

    abstract class RunningDatabase : RoomDatabase() {
        abstract fun getRunDao(): GameStatDao
    }

    abstract fun gameStatDao(): GameStatDao

    companion object {

        @Volatile
        private var INSTANCE: GameDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        GameDatabase::class.java,
                        "stats_data_database"
                    ).build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
}