package com.example.trackpace.repository

import com.example.trackpace.db.RunDatabase
import com.example.trackpace.models.Run

class RunRepository(
    val db: RunDatabase
) {
    suspend fun addRun(run: Run) = db.getRunDao().insertRun(run)

    suspend fun deleteRun(run: Run) = db.getRunDao().deleteRun(run)

    suspend fun getRuns() = db.getRunDao().getRun()

}