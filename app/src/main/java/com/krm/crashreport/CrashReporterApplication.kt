package com.krm.crashreport

import android.app.Application
import com.krm.crashreport.CrashReporter.initialize

class CrashReporterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) { //initialise reporter with external path
            initialize(this)
        }
    }
}