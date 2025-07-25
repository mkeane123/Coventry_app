package com.example.coventry.data.repository

import com.example.coventry.data.model.CallRecord


import android.content.Context
import com.example.coventry.data.local.AppDatabase
import kotlinx.coroutines.flow.Flow

class CallRecordRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).callRecordDao()

    fun getAllCallRecords(): Flow<List<CallRecord>> = dao.getAll()

    suspend fun insert(callRecord: CallRecord ) = dao.insert(callRecord)
}
