package com.yaman.custom_download_manager

import android.Manifest
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.yaman.custom_download_manager.download_work_manager.startDownloadingFile
import com.yaman.custom_download_manager.models.File
import com.yaman.custom_download_manager.models.FileParams
import com.yaman.custom_download_manager.models.NotificationConstants


class MainActivity : AppCompatActivity() {

    private lateinit var requestMultiplePermission: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // register for permissions (Storage)
        requestMultiplePermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            var isGranted = false

            for (item in it) {
                isGranted = item.value
            }

            if (!isGranted) {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
        val progressBar = ProgressDialog(this)
        progressBar.setCancelable(true) //you can cancel it by pressing back button.
        progressBar.setMessage("File downloading ...")
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressBar.show()

        //ask for permissions
        requestMultiplePermission.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

        NotificationConstants.ENABLE_FOREGROUND_NOTIFICATION = false

        startDownloadingFile(
            this,
            this,
            File(
                "10",
                "video file",
                "MP4",
                "http://techslides.com/demos/sample-videos/small.mp4",
            ),
            success = { status, workInfo ->
                Log.e("startDownloadingFile", "sucesss: $workInfo")
                progressBar.dismiss()
            },
            failed = { status, workInfo ->
                Log.e("startDownloadingFile", "failed: $workInfo")
                progressBar.dismiss()
            },
            running = { workInfo ->
                Log.e("startDownloadingFile", "YAMAN: $workInfo")
                Log.e(
                    "startDownloadingFile",
                    "running: ${workInfo.progress.getString(FileParams.KEY_FILE_PROGRESS)}"
                )
                if (workInfo.progress.getString(FileParams.KEY_FILE_PROGRESS) != null) {
                    progressBar.progress =
                        workInfo.progress.getString(FileParams.KEY_FILE_PROGRESS)?.toInt()!!
                }
            },
            cancelled = {

            }
        )


    }


}