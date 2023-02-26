package com.yaman.myapplication.room_db

import androidx.room.RoomDatabase

abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}