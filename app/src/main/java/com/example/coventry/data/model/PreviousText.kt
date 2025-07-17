
package com.example.coventry.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "texts")
data class PreviousText(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String,
    val body: String,
    val timestamp: Long,

)
