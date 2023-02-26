package com.yaman.myapplication.video_download_sample

import android.util.Log
//import com.yaman.custom_download_manager.download_work_manager.DownloadLiveDataHelper
//import com.yaman.custom_download_manager.download_work_manager.DownloadVideoListener
import com.yaman.library_tools.app_utils.generic_recycler_view.recycler_view_homogenous.view_holders.BaseViewHolder
import com.yaman.library_tools.app_utils.generic_recycler_view.recycler_view_homogenous.adapters.GenericAdapter
import com.yaman.myapplication.R
import com.yaman.myapplication.databinding.VideoItemBinding
import com.yaman.myapplication.room_db.models.VideoDownload


class VideosAdapter(val listenerDownloadStart: (VideoDownload)-> Unit) : GenericAdapter<VideoDownload>(
    R.layout.video_item){

    override fun onBindViewHold(holder: BaseViewHolder<VideoDownload>, position: Int) {
        val bind = holder.binding as VideoItemBinding
        bind.videoTitle.text = getItem(position).title

        bind.downloadStart.setOnClickListener {
            if(!getItem(position).isDownloading){
                listenerDownloadStart(getItem(position))
            }
        }

//        DownloadLiveDataHelper().observePercentage(object : DownloadVideoListener {
//            override fun percentage(percent: Int) {
//                Log.e("TAG", "percentage one: ${percent}" )
//                Log.e("TAG", "WorkerParameters: ${getItem(position).workerId}" )
////
////                if(getItem(position).workerId.equals(workerParameters.id.toString(),ignoreCase = true)){
////                    Log.e("TAG", "percentage: two" )
////                    bind.progressBar.progress = percent
////                }
//
//            }
//        })

    }



}
