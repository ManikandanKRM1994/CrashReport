package com.krm.crashreport.utils

import android.Manifest
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import java.util.*

object AppUtils {
    private fun getCurrentLauncherApp(context: Context): String {
        var str = ""
        val localPackageManager = context.packageManager
        val intent = Intent("android.intent.action.MAIN")
        intent.addCategory("android.intent.category.HOME")
        try {
            val resolveInfo = localPackageManager.resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if (resolveInfo?.activityInfo != null) {
                str = resolveInfo.activityInfo.packageName
            }
        } catch (e: Exception) {
            Log.e("AppUtils", "Exception : " + e.message)
        }
        return str
    }

    private fun getUserIdentity(context: Context): String {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) ==
                PackageManager.PERMISSION_GRANTED) {
            val manager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
            val list = manager.accounts
            var emailId: String? = null
            for (account in list) {
                if (account.type.equals("com.google", ignoreCase = true)) {
                    emailId = account.name
                    break
                }
            }
            if (emailId != null) {
                return emailId
            }
        }
        return ""
    }

    @JvmStatic
    fun getDeviceDetails(context: Context): String {
        return ("Device Information\n"
                + "\nDEVICE.ID : " + getDeviceId(context)
                + "\nUSER.ID : " + getUserIdentity(context)
                + "\nAPP.VERSION : " + getAppVersion(context)
                + "\nLAUNCHER.APP : " + getCurrentLauncherApp(context)
                + "\nTIMEZONE : " + timeZone()
                + "\nVERSION.RELEASE : " + Build.VERSION.RELEASE
                + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
                + "\nBOARD : " + Build.BOARD
                + "\nBOOTLOADER : " + Build.BOOTLOADER
                + "\nBRAND : " + Build.BRAND
                + "\nCPU_ABI : " + Build.CPU_ABI
                + "\nCPU_ABI2 : " + Build.CPU_ABI2
                + "\nDISPLAY : " + Build.DISPLAY
                + "\nFINGERPRINT : " + Build.FINGERPRINT
                + "\nHARDWARE : " + Build.HARDWARE
                + "\nHOST : " + Build.HOST
                + "\nID : " + Build.ID
                + "\nMANUFACTURER : " + Build.MANUFACTURER
                + "\nMODEL : " + Build.MODEL
                + "\nPRODUCT : " + Build.PRODUCT
                + "\nSERIAL : " + Build.SERIAL
                + "\nTAGS : " + Build.TAGS
                + "\nTIME : " + Build.TIME
                + "\nTYPE : " + Build.TYPE
                + "\nUNKNOWN : " + Build.UNKNOWN
                + "\nUSER : " + Build.USER)
    }

    private fun timeZone(): String {
        val tz = TimeZone.getDefault()
        return tz.id
    }

    private fun getDeviceId(context: Context): String {
        var androidDeviceId = getAndroidDeviceId(context)
        if (androidDeviceId == null) androidDeviceId = UUID.randomUUID().toString()
        return androidDeviceId
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidDeviceId(context: Context): String? {
        val mInvalidAndroidID = "9774d56d682e549c"
        val androidId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID)
        return if (androidId == null
                || androidId.toLowerCase() == mInvalidAndroidID) {
            null
        } else androidId
    }

    private fun getAppVersion(context: Context): Int {
        return try {
            val packageInfo = context.packageManager
                    .getPackageInfo(context.packageName, 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Could not get package name: $e")
        }
    }
}