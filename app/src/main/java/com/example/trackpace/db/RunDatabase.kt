package com.example.trackpace.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trackpace.daos.RunDao
import com.example.trackpace.models.Run

@Database(entities = [Run::class],
version = 1)
abstract class RunDatabase : RoomDatabase() {
    abstract fun getRunDao():RunDao

    companion object{
        fun getInstance(context: Context): RunDatabase{
            return Room.databaseBuilder(context,RunDatabase::class.java,"RunDatabase").build()
        }
    }

}