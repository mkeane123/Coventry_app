package com.example.coventry.data.repository


import android.content.Context
import com.example.coventry.data.local.AppDatabase
import com.example.coventry.data.model.PreviousText
import kotlinx.coroutines.flow.Flow

class PreviousTextRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).previousTextDao()

    fun getAllTexts(): Flow<List<PreviousText>> = dao.getAll()

    suspend fun insert(text: PreviousText) = dao.insert(text)
}
