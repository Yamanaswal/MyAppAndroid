package com.yaman.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.yaman.myapplication.databinding.ActivityMainBinding
import com.yaman.myapplication.room_db.AppDatabase
import com.yaman.myapplication.video_download_sample.DownloadVideosActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    @Inject
//    lateinit var roomDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        Log.e("Storage: ", "onCreate: " + Environment.getExternalStorageDirectory())
        Log.e(
            "Storage: ",
            "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS)
        )
        Log.e(
            "Storage: ",
            "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        )
        Log.e(
            "Storage: ",
            "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        )
        Log.e(
            "Storage: ",
            "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        )
        Log.e(
            "Storage: ",
            "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        )
        Log.e(
            "Storage: ",
            "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS)
        )
        Log.e(
            "Storage: ",
            "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        )

        Log.e("Storage: ", "onCreate: " + cacheDir)
        Log.e("Storage: ", "onCreate: " + filesDir)
        Log.e("Storage: ", "onCreate: " + filesDir.parent)
        Log.e("Storage: ", "onCreate: " + externalCacheDir)
        Log.e("Storage: ", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_ALARMS))
        Log.e("Storage: ", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_MOVIES))
        Log.e("Storage: ", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS))
        Log.e("Storage: ", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))

        startActivity(Intent(this, DownloadVideosActivity::class.java))
    }
}