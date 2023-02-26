package com.yaman.myapplication.room_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yaman.myapplication.room_db.models.VideoDownload

@Dao
interface DatabaseDao  {

    @Insert
    fun addVideo(videoDownload: VideoDownload)

    @Query("SELECT * FROM VideoDownload")
    fun getAll(): List<VideoDownload>
//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

//    @Delete
//    fun delete(user: User)
}