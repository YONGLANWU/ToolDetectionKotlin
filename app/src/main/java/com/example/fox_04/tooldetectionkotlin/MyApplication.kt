package com.example.fox_04.tooldetectionkotlin

import android.app.Application

/**
 * Created by fox_04 on 2018/3/23.
 */
class MyApplication : Application() {

    companion object {
        private var sInstance: MyApplication? = null
        fun instance() = sInstance
    }
    override fun onCreate() {
        super.onCreate()
        sInstance = this;
    }
}