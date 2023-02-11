package com.yaman.exoplayer_custom

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val playerView = findViewById<StyledPlayerView>(R.id.player_view)

        val player = ExoPlayerManager(this).apply {
            setSingleMediaItem(Uri.parse("http://techslides.com/demos/sample-videos/small.mp4"))
        }
        playerView.player = player.getPlayer()
    }
}