package com.logger.service

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null
        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize any required components here
    }
}
