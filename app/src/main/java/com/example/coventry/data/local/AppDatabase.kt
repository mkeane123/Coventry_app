package com.example.coventry.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coventry.data.model.PreviousText

@Database(entities = [PreviousText::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun previousTextDao(): PreviousTextDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coventry_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
