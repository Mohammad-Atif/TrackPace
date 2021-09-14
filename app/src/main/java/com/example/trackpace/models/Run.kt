package com.example.trackpace.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Run(
    val imageUri: String?,
    val date: String?,
    val distance: String?,
    val duration: String?,
    val calories : String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)