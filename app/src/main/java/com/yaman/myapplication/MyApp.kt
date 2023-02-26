package com.yaman.myapplication

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.yaman.myapplication.exception_handler.ExceptionHandlerActivity
import com.yaman.myapplication.exception_handler.ExceptionListener
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), ExceptionListener {

    override fun onCreate() {
        super.onCreate()
        setupExceptionHandler()
    }

    private fun setupExceptionHandler(){
        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    uncaughtException(Looper.getMainLooper().thread, e)
                }
            }
        }
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            uncaughtException(t, e)
        }
    }


    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        // TODO Make sure you are logging this issue some where like Crashlytics.
        // Also indicate that something went wrong to the user like maybe a dialog or an activity.
        Log.e("MyApp", throwable.message.toString())
        startActivity(Intent(this,ExceptionHandlerActivity::class.java))
    }

    companion object {

    }

}