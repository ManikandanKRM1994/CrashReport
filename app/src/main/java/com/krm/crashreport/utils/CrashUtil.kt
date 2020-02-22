package com.krm.crashreport.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.krm.crashreport.CrashReporter.context
import com.krm.crashreport.CrashReporter.crashReportPath
import com.krm.crashreport.CrashReporter.isNotificationEnabled
import com.krm.crashreport.CrashReporter.launchIntent
import com.krm.crashreport.R
import com.krm.crashreport.utils.Constants.CHANNEL_NOTIFICATION_ID
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object CrashUtil {
    private val TAG = CrashUtil::class.java.simpleName
    private val crashLogTime: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return dateFormat.format(Date())
        }

    fun saveCrashReport(throwable: Throwable) {
        val crashReportPath = crashReportPath
        val filename = crashLogTime + Constants.CRASH_SUFFIX + Constants.FILE_EXTENSION
        writeToFile(crashReportPath, filename, getStackTrace(throwable))
        showNotification(throwable.localizedMessage!!, true)
    }

    fun logException(exception: Exception) {
        Thread(Runnable {
            val crashReportPath = crashReportPath
            val filename = crashLogTime + Constants.EXCEPTION_SUFFIX + Constants.FILE_EXTENSION
            writeToFile(crashReportPath, filename, getStackTrace(exception))
            showNotification(exception.localizedMessage!!, false)
        }).start()
    }

    private fun writeToFile(crashReportPath: String?, filename: String, crashLog: String) {
        var mCrashReportPath = crashReportPath
        if (TextUtils.isEmpty(mCrashReportPath)) {
            mCrashReportPath = defaultPath
        }
        val crashDir = File(mCrashReportPath!!)
        if (!crashDir.exists() || !crashDir.isDirectory) {
            mCrashReportPath = defaultPath
            Log.e(TAG, "Path provided doesn't exists : $crashDir\nSaving crash report at : $defaultPath")
        }
        val bufferedWriter: BufferedWriter
        try {
            bufferedWriter = BufferedWriter(FileWriter(
                    mCrashReportPath + File.separator + filename))
            bufferedWriter.write(crashLog)
            bufferedWriter.flush()
            bufferedWriter.close()
            Log.d(TAG, "crash report saved in : $mCrashReportPath")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNotification(localisedMsg: String, isCrash: Boolean) {
        if (isNotificationEnabled) {
            val context = context
            val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(notificationManager, context)
            val builder = NotificationCompat.Builder(context, CHANNEL_NOTIFICATION_ID)
            builder.setSmallIcon(R.drawable.ic_warning_black_24dp)
            val intent = launchIntent
            intent.putExtra(Constants.LANDING, isCrash)
            intent.action = System.currentTimeMillis().toString()
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            builder.setContentIntent(pendingIntent)
            builder.setContentTitle(context.getString(R.string.view_crash_report))
            if (TextUtils.isEmpty(localisedMsg)) {
                builder.setContentText(context.getString(R.string.check_your_message_here))
            } else {
                builder.setContentText(localisedMsg)
            }
            builder.setAutoCancel(true)
            builder.color = ContextCompat.getColor(context, R.color.colorAccent)
            notificationManager.notify(Constants.NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager, context: Context?) {
        if (Build.VERSION.SDK_INT >= 26) {
            val name: CharSequence = context!!.getString(R.string.notification_crash_report_title)
            val description = ""
            val channel = NotificationChannel(CHANNEL_NOTIFICATION_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getStackTrace(e: Throwable): String {
        val result: Writer = StringWriter()
        val printWriter = PrintWriter(result)
        e.printStackTrace(printWriter)
        val crashLog = result.toString()
        printWriter.close()
        return crashLog
    }

    @JvmStatic
    val defaultPath: String
        get() {
            val defaultPath = (context!!.getExternalFilesDir(null)!!.absolutePath
                    + File.separator + Constants.CRASH_REPORT_DIR)
            val file = File(defaultPath)
            file.mkdirs()
            return defaultPath
        }
}