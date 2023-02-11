package com.yaman.custom_download_manager.download_work_manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.yaman.custom_download_manager.models.NotificationConstants.ENABLE_FOREGROUND_NOTIFICATION
import com.yaman.custom_download_manager.download_work_manager.DownloadUtils.getSavedFileUri
import com.yaman.custom_download_manager.models.FileParams
import com.yaman.custom_download_manager.models.NotificationConstants


class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    var progress = 0

    override suspend fun doWork(): Result {

        val fileUrl = inputData.getString(FileParams.KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(FileParams.KEY_FILE_NAME) ?: ""
        val fileType = inputData.getString(FileParams.KEY_FILE_TYPE) ?: ""
        val fileDownloadPath = inputData.getString(FileParams.KEY_FILE_DOWNLOAD_PATH) ?: ""

        Log.d("TAG", "doWork: $fileUrl | $fileName | $fileType")

        if (fileName.isEmpty() || fileType.isEmpty() || fileUrl.isEmpty()) {
            Result.failure()
        }

        if (ENABLE_FOREGROUND_NOTIFICATION) {
            val notificationChannel = createNotificationChannel()
            NotificationManagerCompat.from(context)
                .notify(NotificationConstants.NOTIFICATION_ID, notificationChannel)
        }

        try {

            val uri = getSavedFileUri(
                fileName = fileName,
                fileType = fileType,
                fileUrl = fileUrl,
                downloadPath = fileDownloadPath,
                context = context
            ) {
                setProgressAsync(workDataOf(FileParams.KEY_FILE_PROGRESS to it.toString()))
                progress = it
            }

            return if (uri != null) {
                Result.success(workDataOf(FileParams.KEY_FILE_URI to uri.toString()))
            } else {
                Result.failure()
            }


        } catch (e: Exception) {
            Log.e("getSavedFileUri", "doWork - Exception: ${e.message}")
            Log.e("getSavedFileUri", "doWork - Exception: ${e.localizedMessage}")
            return Result.failure()
        }
        finally {
            if (ENABLE_FOREGROUND_NOTIFICATION) {
                NotificationManagerCompat.from(context).cancel(NotificationConstants.NOTIFICATION_ID)
            }
        }

    }


    private fun createNotificationChannel(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = NotificationConstants.CHANNEL_NAME
            val description = NotificationConstants.CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID, name, importance)
            channel.description = description

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            notificationManager?.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(com.google.android.material.R.drawable.ic_clock_black_24dp)
            .setContentTitle("Downloading your file...")
            .setOngoing(true)
            .setProgress(100, 0, true)


        return builder.build()
    }


}