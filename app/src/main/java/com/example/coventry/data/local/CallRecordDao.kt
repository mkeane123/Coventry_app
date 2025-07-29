package com.example.coventry.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coventry.data.model.CallRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface CallRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: CallRecord)

    @Query("SELECT * FROM call_records ORDER BY startTime ASC")
    fun getAll(): Flow<List<CallRecord>>//List<CallRecord>

    @Query("DELETE FROM call_records")
    suspend fun clearAll()


}