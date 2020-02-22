package com.krm.crashreport.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.krm.crashreport.CrashReporter.crashReportPath
import com.krm.crashreport.adapter.MainPagerAdapter
import com.krm.crashreport.utils.Constants
import com.krm.crashreport.utils.CrashUtil.defaultPath
import com.krm.crashreport.utils.FileUtils.delete
import com.krm.crashreport.utils.SimplePageChangeListener
import com.google.android.material.tabs.TabLayout
import com.krm.crashreport.R
import java.io.File

class CrashReporterActivity : AppCompatActivity() {
    private var mainPagerAdapter: MainPagerAdapter? = null
    private var selectedTabPosition = 0
    //region activity callbacks
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.log_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.delete_crash_logs) {
            clearCrashLog()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crash_reporter_activity)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.crash_reporter)
        toolbar.subtitle = applicationName
        setSupportActionBar(toolbar)
        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        setupViewPager(viewPager)
        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

    //endregion
    private fun clearCrashLog() {
        Thread(Runnable {
            val crashReportPath = if (TextUtils.isEmpty(crashReportPath)) defaultPath else crashReportPath!!
            val logs = File(crashReportPath).listFiles()
            for (file in logs) {
                delete(file!!)
            }
            runOnUiThread { mainPagerAdapter!!.clearLogs() }
        }).start()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val titles = arrayOf(getString(R.string.crashes), getString(R.string.exceptions))
        mainPagerAdapter = MainPagerAdapter(supportFragmentManager, titles)
        viewPager.adapter = mainPagerAdapter
        viewPager.addOnPageChangeListener(object : SimplePageChangeListener() {
            override fun onPageSelected(position: Int) {
                selectedTabPosition = position
            }
        })
        val intent = intent
        if (intent != null && !intent.getBooleanExtra(Constants.LANDING, false)) {
            selectedTabPosition = 1
        }
        viewPager.currentItem = selectedTabPosition
    }

    private val applicationName: String
        get() {
            val applicationInfo = applicationInfo
            val stringId = applicationInfo.labelRes
            return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else getString(stringId)
        }
}