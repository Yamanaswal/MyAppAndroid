package com.yaman.myapplication.video_download_sample

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.work.WorkInfo
import com.yaman.custom_download_manager.download_work_manager.startDownloadingFile
import com.yaman.custom_download_manager.models.File
import com.yaman.custom_download_manager.models.FileParams
import com.yaman.custom_download_manager.models.NotificationConstants
import com.yaman.myapplication.R
import com.yaman.myapplication.databinding.ActivityDownloadVideosBinding
import com.yaman.myapplication.room_db.models.VideoDownload


class DownloadVideosActivity : AppCompatActivity() {

    private lateinit var requestMultiplePermission: ActivityResultLauncher<Array<String>>
    private lateinit var binding: ActivityDownloadVideosBinding

    private val adapter = VideosAdapter({
        startVideoDownload(it)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_download_videos)

        binding.recyclerView.adapter = adapter
        val list = mutableListOf<VideoDownload>()
        val video1 = VideoDownload(id = "1", title = "Video title 1")
        val video2 = VideoDownload(id = "2", title = "Video title 2")
        val video3 = VideoDownload(id = "3", title = "Video title 3")
        list.add(video1)
        list.add(video2)
        list.add(video3)
        adapter.updateList(list)

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

        //ask for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestMultiplePermission.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }else{
            requestMultiplePermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

        NotificationConstants.ENABLE_FOREGROUND_NOTIFICATION = true

    }


    private fun startVideoDownload(videoData: VideoDownload) {

        startDownloadingFile(
            this,
            this,
            File(
                videoData.id,
                videoData.title,
                "MP4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            ),
            enqueued = { workInfo : WorkInfo ->

            },
            success = { status, workInfo ->
                Log.e("startDownloadingFile", "sucesss: $workInfo")
            },
            failed = { status, workInfo ->
                Log.e("startDownloadingFile", "failed: $workInfo")
            },
            running = { workInfo ->
                Log.e("startDownloadingFile", "YAMAN: $workInfo")
                Log.e(
                    "startDownloadingFile",
                    "running: ${workInfo.progress.getString(FileParams.KEY_FILE_PROGRESS)}"
                )
//                if (workInfo.progress.getString(FileParams.KEY_FILE_PROGRESS) != null) {
////                    progressBar.progress =
////                        workInfo.progress.getString(FileParams.KEY_FILE_PROGRESS)?.toInt()!!
//                }
            },
            cancelled = {

            }
        )

    }


}