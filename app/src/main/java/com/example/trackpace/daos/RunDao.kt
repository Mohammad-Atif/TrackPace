package com.example.trackpace.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trackpace.models.Run


@Dao
interface RunDao {
    @Insert
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("select * from Run")
    suspend fun getRun(): List<Run>

}