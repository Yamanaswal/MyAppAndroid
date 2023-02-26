package com.yaman.custom_download_manager.download_work_manager

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.yaman.custom_download_manager.models.File
import com.yaman.custom_download_manager.models.FileParams


fun startDownloadingFile(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    file: File,
    storageNotLow: Boolean = true,
    batteryNotLow: Boolean = true,
    enqueued: (workInfo: WorkInfo) -> Unit,
    success: (status: String, workInfo: WorkInfo) -> Unit,
    failed: (status: String, workInfo: WorkInfo) -> Unit,
    running: (workInfo: WorkInfo) -> Unit,
    cancelled: (workInfo: WorkInfo) -> Unit,
): WorkManager {
    val data = Data.Builder()

    data.apply {
        putString(FileParams.KEY_FILE_NAME, file.name)
        putString(FileParams.KEY_FILE_URL, file.url)
        putString(FileParams.KEY_FILE_TYPE, file.type)
        putString(FileParams.KEY_FILE_DOWNLOAD_PATH, file.downloadPath)
    }

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresStorageNotLow(storageNotLow)
        .setRequiresBatteryNotLow(batteryNotLow)
        .build()

    val fileDownloadWorker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
        .setConstraints(constraints)
        .setInputData(data.build())
        .build()

    val workManager = WorkManager.getInstance(context)

    workManager.enqueueUniqueWork(
        "oneFileDownloadWork_${System.currentTimeMillis()}",
        ExistingWorkPolicy.KEEP,
        fileDownloadWorker
    )

    workManager.getWorkInfoByIdLiveData(fileDownloadWorker.id)
        .observe(lifecycleOwner) { info ->
            info?.let {
                when (it.state) {
                    WorkInfo.State.ENQUEUED -> {
                        Log.e("ENQUEUED", "fileDownloadWorker.id: ${fileDownloadWorker.id}")
                        enqueued(it)
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        success(it.outputData.getString(FileParams.KEY_FILE_URI) ?: "", it)
                    }
                    WorkInfo.State.FAILED -> {
                        failed("Downloading failed!!", it)
                    }
                    WorkInfo.State.RUNNING -> {
                        running(it)
                    }
                    WorkInfo.State.CANCELLED -> {
                        cancelled(it)
                    }
                    else -> {
                        Log.e("TAG", "startDownloadingFile: No State Found.")
                    }
                }
            }
        }

    return workManager
}