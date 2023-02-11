package com.yaman.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("Storage: ", "onCreate: " + Environment.getExternalStorageDirectory())
        Log.e("Storage: ", "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS))
        Log.e("Storage: ", "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
        Log.e("Storage: ", "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))
        Log.e("Storage: ", "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS))
        Log.e("Storage: ", "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC))
        Log.e("Storage: ", "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS))
        Log.e("Storage: ", "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES))

        Log.e("Storage: ", "onCreate: " + cacheDir)
        Log.e("Storage: ", "onCreate: " + filesDir)
        Log.e("Storage: ", "onCreate: " + filesDir.parent)
        Log.e("Storage: ", "onCreate: " + externalCacheDir)
        Log.e("Storage: ", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_ALARMS))
        Log.e("Storage: ", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_MOVIES))
        Log.e("Storage: ", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS))
        Log.e("Storage: ", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))

        startActivity(Intent(this,com.yaman.custom_download_manager.MainActivity::class.java))
//        startActivity(Intent(this,com.yaman.exoplayer_custom.MainActivity::class.java))
    }
}