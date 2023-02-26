package com.yaman.custom_download_manager.download_work_manager

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.work.WorkerParameters
import com.yaman.custom_download_manager.models.FileDownloadPaths.ExternalStorageDirectory
import java.io.*
import java.io.File
import java.net.URL

object DownloadUtils {

    /*
    The getSavedFileUri() function will return a URI if the file has been saved successfully,
    otherwise, it will return null.
    * */
    fun getSavedFileUri(
        fileName: String,
        fileType: String,
        fileUrl: String,
        downloadPath: String,
        context: Context,
        workerParameters: WorkerParameters,
        listener: (Int) -> Unit
    ): Uri? {

        // different types of files will have different mime type
        val mimeType = when (fileType) {
            "PDF" -> "application/pdf"
            "PNG" -> "image/png"
            "MP4" -> "video/mp4"
            else -> ""
        }

        if (mimeType.isEmpty()) {
            Log.e("mimeType: ", "getSavedFileUri: please provide valid fileType (mimeType)")
            return null
        }

        // if Android 10 or Above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/DownloaderDemo")
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            return if (uri != null) {
                val url = URL(fileUrl)
                getProgress(url.openStream(), url.openConnection().contentLength,workerParameters,listener)

                url.openStream().use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                    }
                }
                uri
            } else {
                null
            }

        }
        // if Android 9 or below
        else {

            val target = setDownloadPaths(downloadPath,fileName)

            val url = URL(fileUrl)
            getProgress(url.openStream(), url.openConnection().contentLength,workerParameters,listener)

            url.openStream().use { input ->
                FileOutputStream(target).use { output ->
                    input.copyTo(output)
                }
            }

            return target.toUri()
        }
    }

    private fun setDownloadPaths(downloadPath: String, fileName: String): File {
        return when {
            downloadPath.equals(ExternalStorageDirectory, ignoreCase = true) -> {
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS),
                    fileName
                )
            }
            else -> {
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS),
                    fileName
                )
            }
        }


    }


    private fun getProgress(inputStream: InputStream, contentLength: Int,
                            workerParameters:WorkerParameters, listener: (Int) -> Unit) {
        try {
            val total: Long = contentLength.toLong()
            var count: Int
            val input: InputStream = BufferedInputStream(inputStream)
            val data = ByteArray(4096)
            var current: Long = 0
            while (input.read(data).also { count = it } != -1) {
                current += count.toLong()
                val percentage = (current * 100 / total).toInt()
                listener(percentage)
            }
            input.close()
        } catch (e: Exception) {
            Log.e("Error: ", "getProgress: ${e.localizedMessage}")
        }
    }

}