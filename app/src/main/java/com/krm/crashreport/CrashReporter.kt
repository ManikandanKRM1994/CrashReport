package com.krm.crashreport

import android.content.Context
import android.content.Intent
import com.krm.crashreport.ui.CrashReporterActivity
import com.krm.crashreport.utils.CrashReporterExceptionHandler
import com.krm.crashreport.utils.CrashReporterNotInitializedException
import com.krm.crashreport.utils.CrashUtil

object CrashReporter {
    private var applicationContext: Context? = null
    @JvmStatic
    var crashReportPath: String? = null
        private set
    var isNotificationEnabled = true
        private set

    fun initialize(context: Context?) {
        applicationContext = context
        setUpExceptionHandler()
    }

    fun initialize(context: Context?, crashReportSavePath: String?) {
        applicationContext = context
        crashReportPath = crashReportSavePath
        setUpExceptionHandler()
    }

    private fun setUpExceptionHandler() {
        if (Thread.getDefaultUncaughtExceptionHandler() !is CrashReporterExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(CrashReporterExceptionHandler())
        }
    }

    @JvmStatic
    val context: Context?
        get() {
            if (applicationContext == null) {
                try {
                    throw CrashReporterNotInitializedException("Initialize CrashReporter : call CrashReporter.initialize(context, crashReportPath)")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return applicationContext
        }

    //LOG Exception APIs
    @JvmStatic
    fun logException(exception: Exception?) {
        CrashUtil.logException(exception!!)
    }

    @JvmStatic
    val launchIntent: Intent
        get() = Intent(applicationContext, CrashReporterActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    fun disableNotification() {
        isNotificationEnabled = false
    }
}