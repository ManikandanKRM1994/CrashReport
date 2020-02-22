package com.krm.crashreport.utils

import android.text.TextUtils
import com.krm.crashreport.utils.CrashUtil.defaultPath
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

object FileUtils {
    fun delete(absPath: String?): Boolean {
        if (TextUtils.isEmpty(absPath)) {
            return false
        }
        val file = File(absPath!!)
        return delete(file)
    }

    @JvmStatic
    fun delete(file: File): Boolean {
        if (!exists(file)) {
            return true
        }
        if (file.isFile) {
            return file.delete()
        }
        var result = true
        val files = file.listFiles() ?: return false
        for (index in files.indices) {
            result = result or delete(files[index])
        }
        result = result or file.delete()
        return result
    }

    private fun exists(file: File?): Boolean {
        return file != null && file.exists()
    }

    private fun cleanPath(absPath: String?): String? {
        var mAbsPath = absPath
        if (TextUtils.isEmpty(mAbsPath)) {
            return mAbsPath
        }
        try {
            val file = File(mAbsPath!!)
            mAbsPath = file.canonicalPath
        } catch (e: Exception) {
        }
        return mAbsPath
    }

    private fun getParent(file: File?): String? {
        return file?.parent
    }

    fun getParent(absPath: String?): String? {
        var mAbsPath = absPath
        if (TextUtils.isEmpty(mAbsPath)) {
            return null
        }
        mAbsPath = cleanPath(mAbsPath)
        val file = File(mAbsPath!!)
        return getParent(file)
    }

    fun deleteFiles(directoryPath: String?): Boolean {
        val directoryToDelete: String? = if (!TextUtils.isEmpty(directoryPath)) {
            directoryPath
        } else {
            defaultPath
        }
        return delete(directoryToDelete)
    }

    @JvmStatic
    fun readFirstLineFromFile(file: File?): String {
        var line = ""
        try {
            val reader = BufferedReader(FileReader(file!!))
            line = reader.readLine()
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return line
    }

    @JvmStatic
    fun readFromFile(file: File?): String {
        val crash = StringBuilder()
        try {
            val reader = BufferedReader(FileReader(file!!))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                crash.append(line)
                crash.append('\n')
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return crash.toString()
    }
}