package com.yaman.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yaman.myapplication.aes.AES
import java.io.File


class DownloadActivity : AppCompatActivity() {

    private var referenceID = -1L

    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        setDownloadManager()

       if (checkPermission())
        {

            /*** If Storage Permission Is Given, Check External storage is available for read and write***/
//            Uri image_uri = Uri.parse("https://unifiedclothes.com/Unifiedclothes/App_Gallery/thumb_8_121432471036-1432471036-SC-505.jpg");
//
//            referenceID = DownloadImage(image_uri);
//
            startDownload()
        } else {
            requestPermission()
        }

        val d = DataClass()
        val s = AES.encrypt(d.toString())
        Log.e("ENC - ", "onCreate: $s")
        val z = AES.decrypt(s)
        Log.e("DEC - ", "onCreate: $z")
    }

    data class DataClass(val i: Int = 6, val s: String = "Hii")

    private fun setDownloadManager() {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        registerReceiver(
            downloadManagerReceiver,
            IntentFilter().apply {
                addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
                addAction(DownloadManager.ACTION_VIEW_DOWNLOADS)
            }
        )


        registerReceiver(
            onNotificationClick,
            IntentFilter(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
        )

        registerReceiver(
            onNotificationClick,
            IntentFilter(DownloadManager.COLUMN_DESCRIPTION)
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadManagerReceiver)
        unregisterReceiver(onNotificationClick)
    }

    private val downloadManagerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(this@DownloadActivity, "OnComplete", Toast.LENGTH_SHORT).show()

            queryStatus()
            if (intent?.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

            } else if (intent?.action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

            } else if (intent?.action.equals(DownloadManager.ACTION_VIEW_DOWNLOADS)) {

            }
        }
    }

    private val onNotificationClick = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(this@DownloadActivity, "onNotificationClick", Toast.LENGTH_SHORT).show()
        }
    }

    fun startDownload() {
        val uri: Uri = Uri.parse("http://commonsware.com/misc/test.mp4")

        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .mkdirs()

        referenceID = downloadManager.enqueue(
            DownloadManager.Request(uri)
                .setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
                )
                .setAllowedOverRoaming(false)
                .setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setAllowedOverMetered(true)
                .setMimeType("")
                .setNotificationVisibility(1)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "test.mp4"
                )
        )

        val file = File(this.filesDir, "filename")

        Log.e("PATH", "startDownload: ${file.path}")
        Log.e("PATH", "startDownload: ${file.absolutePath}")


    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this@DownloadActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@DownloadActivity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val imageUri = Uri.parse("https://www.dccomics.com/sites/default/files/Char_GetToKnow_Batman80_5ca54cb83a27a6.53173051.png")
                 startDownload()
            } else {
                Toast.makeText(
                    this@DownloadActivity,
                    "Permission Denied... \n You Should Allow External Storage Permission To Download Images.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun queryStatus() {
        val cursor: Cursor? =
            downloadManager.query(DownloadManager.Query().setFilterById(referenceID))

        if (cursor == null) {
            Toast.makeText(this, "Download not found!", Toast.LENGTH_LONG).show()
        } else {
            cursor.moveToFirst()

            Log.e(
                javaClass.name, "COLUMN_ID: " + cursor.getColumnIndex(DownloadManager.COLUMN_ID)
            )

            Log.e(
                javaClass.name,
                "COLUMN_BYTES_DOWNLOADED_SO_FAR: " + cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
            )
            Log.e(
                javaClass.name,
                "COLUMN_LAST_MODIFIED_TIMESTAMP: " + cursor.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)
            )
            Log.e(
                javaClass.name,
                "COLUMN_LOCAL_URI: " + cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            )
            Log.e(
                javaClass.name,
                "COLUMN_STATUS: " + cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            )
            Log.e(
                javaClass.name,
                "COLUMN_REASON: " + cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
            )

            Toast.makeText(this, statusMessage(cursor), Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("Range")
    private fun statusMessage(c: Cursor): String {
        var msg = "???"
        msg =
            when (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_FAILED -> "Download failed!"
                DownloadManager.STATUS_PAUSED -> "Download paused!"
                DownloadManager.STATUS_PENDING -> "Download pending!"
                DownloadManager.STATUS_RUNNING -> "Download in progress!"
                DownloadManager.STATUS_SUCCESSFUL -> "Download complete!"
                else -> "Download is nowhere in sight"
            }
        return msg
    }


    fun goToDownloads() {
        startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
    }

    companion object {
        val PERMISSION_REQUEST_CODE = 100
    }

}