package com.krm.crashreport.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.krm.crashreport.utils.AppUtils.getDeviceDetails
import com.krm.crashreport.utils.FileUtils.delete
import com.krm.crashreport.utils.FileUtils.readFromFile
import com.krm.crashreport.R
import java.io.File

class LogMessageActivity : AppCompatActivity() {
    private var appInfo: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_message)
        appInfo = findViewById<View>(R.id.appInfo) as TextView
        val intent = intent
        if (intent != null) {
            val dirPath = intent.getStringExtra("LogMessage")
            val file = File(dirPath!!)
            val crashLog = readFromFile(file)
            val textView = findViewById<View>(R.id.logMessage) as TextView
            textView.text = crashLog
        }
        val myToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        myToolbar.title = getString(R.string.crash_reporter)
        setSupportActionBar(myToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getAppInfo()
    }

    private fun getAppInfo() {
        appInfo!!.text = getDeviceDetails(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.crash_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = intent
        var filePath: String? = null
        if (intent != null) {
            filePath = intent.getStringExtra("LogMessage")
        }
        return when (item.itemId) {
            R.id.delete_log -> {
                if (delete(filePath)) {
                    finish()
                }
                true
            }
            R.id.share_crash_log -> {
                shareCrashReport(filePath)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun shareCrashReport(filePath: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_TEXT, appInfo!!.text.toString())
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(File(filePath!!)))
        startActivity(Intent.createChooser(intent, "Share via"))
    }
}