package com.krm.crashreport

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri
import com.krm.crashreport.CrashReporter.initialize

class CrashReporterInitProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        initialize(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun attachInfo(context: Context, providerInfo: ProviderInfo) {
        // So if the authorities equal the library internal ones, the developer forgot to set his applicationId
        check("com.krm.crashreport.CrashReporterInitProvider" != providerInfo.authority) {
            ("Incorrect provider authority in manifest. Most likely due to a "
                    + "missing applicationId variable in application\'s build.gradle.")
        }
        super.attachInfo(context, providerInfo)
    }
}