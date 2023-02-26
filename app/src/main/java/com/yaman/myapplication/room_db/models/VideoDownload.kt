package com.yaman.myapplication.room_db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class VideoDownload(
    @PrimaryKey
    var id: String,
    @ColumnInfo
    var title: String,
    @ColumnInfo
    var isDownloading: Boolean = false,
    @ColumnInfo
    var workerId: String = ""
)