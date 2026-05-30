package com.example.trainsmart.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trainsmart.data.dao.SeanceDao
import com.example.trainsmart.data.entity.Seance

@Database(
    entities = [Seance::class],
    version = 1,
    exportSchema = false
)
abstract class TrainSmartDatabase : RoomDatabase() {

    abstract fun seanceDao(): SeanceDao

    companion object {
        @Volatile
        private var INSTANCE: TrainSmartDatabase? = null

        fun getDatabase(context: Context): TrainSmartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrainSmartDatabase::class.java,
                    "trainsmart_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}