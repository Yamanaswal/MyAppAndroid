package com.yaman.custom_download_manager.models

data class File(
    val id: String,
    val name: String,
    val type: String,
    val url: String,
    val downloadPath: String = FileDownloadPaths.ExternalStorageDirectory,
    var downloadedUri: String? = null,
    var isDownloading: Boolean = false,
)