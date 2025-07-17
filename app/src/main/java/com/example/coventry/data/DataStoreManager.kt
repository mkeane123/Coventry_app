package com.example.coventry.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")



class DataStoreManager (private val context: Context) {
    companion object {
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch")
        private val PERMISSIONS_GRANTED_KEY = booleanPreferencesKey("permissions_granted")
    }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { prefs ->
        val value = prefs[FIRST_LAUNCH_KEY] ?: true
        Log.d("DataStoreCheck", "Read firstLaunch = $value")
        value
    }


    val hasPermissions: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PERMISSIONS_GRANTED_KEY] ?: false
    }

    suspend fun setPermissionsGranted(granted: Boolean) {
        context.dataStore.edit { prefs -> prefs[PERMISSIONS_GRANTED_KEY] = granted }
    }


    suspend fun setFirstLaunchDone() {
        context.dataStore.edit { prefs -> prefs[FIRST_LAUNCH_KEY] = false }
    }
}