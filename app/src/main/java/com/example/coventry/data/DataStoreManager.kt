package com.example.coventry.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")



class DataStoreManager (private val context: Context) {
    companion object {
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch")
    }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { prefs -> prefs[FIRST_LAUNCH_KEY] ?: true }


    suspend fun setFirstLaunchDone() {
        context.dataStore.edit { prefs -> prefs[FIRST_LAUNCH_KEY] = false }
    }
}