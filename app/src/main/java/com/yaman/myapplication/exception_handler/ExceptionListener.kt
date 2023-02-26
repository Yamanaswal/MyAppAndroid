package com.yaman.myapplication.exception_handler

interface ExceptionListener {
    fun uncaughtException(thread: Thread, throwable: Throwable)
}
