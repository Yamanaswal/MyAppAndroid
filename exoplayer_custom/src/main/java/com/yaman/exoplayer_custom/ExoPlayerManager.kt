package com.yaman.exoplayer_custom

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class ExoPlayerManager(private val context: Context) {

    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build()
    }

    fun setSingleMediaItem(videoUri: Uri) : MediaItem {
        // Build the media item.
        val mediaItem: MediaItem = MediaItem.fromUri(videoUri)
        // Set the media item to be played.
        exoPlayer.setMediaItem(mediaItem)
        // Prepare the player.
        exoPlayer.prepare()
        // Start the playback.
        exoPlayer.play()
        // get media item
        return mediaItem
    }

    fun setPlaylistMediaItems(videoUris: ArrayList<Uri>): ArrayList<MediaItem> {
        val mediaItemList = ArrayList<MediaItem>()
        mediaItemList.clear()
        // Build the media items.
        videoUris.forEach { videoUri ->
            val mediaItem: MediaItem = MediaItem.fromUri(videoUri)
            // Set the media item to be played.
            exoPlayer.setMediaItem(mediaItem)
            mediaItemList.add(mediaItem)
        }
        // Prepare the player.
        exoPlayer.prepare()
        // Start the playback.
        exoPlayer.play()
        // get media all items
        return mediaItemList
    }

    fun clearPlaylists(){
        exoPlayer.clearMediaItems()
    }
    fun callbacks(){
//        exoPlayer.addListener()
    }

    fun getPlayer(): ExoPlayer {
        return exoPlayer
    }

    fun releasePlayer(){
        exoPlayer.release()
    }
}