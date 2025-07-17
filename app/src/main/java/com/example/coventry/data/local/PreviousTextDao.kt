package com.example.coventry.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.coventry.data.model.PreviousText
import kotlinx.coroutines.flow.Flow

@Dao
interface PreviousTextDao {
    @Insert
    suspend fun insert(text: PreviousText)

    @Query("SELECT * FROM texts ORDER BY timestamp DESC")
    fun getAll(): Flow<List<PreviousText>>
}
