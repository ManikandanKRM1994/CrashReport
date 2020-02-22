package com.krm.crashreport.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krm.crashreport.CrashReporter.crashReportPath
import com.krm.crashreport.adapter.CrashLogAdapter
import com.krm.crashreport.utils.Constants
import com.krm.crashreport.utils.CrashUtil.defaultPath
import com.krm.crashreport.R
import java.io.File
import java.util.*

class CrashLogFragment : Fragment() {
    private var logAdapter: CrashLogAdapter? = null
    private var crashRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.crash_log, container, false)
        crashRecyclerView = view.findViewById<View>(R.id.crashRecyclerView) as RecyclerView
        return view
    }

    override fun onResume() {
        super.onResume()
        loadAdapter(activity, crashRecyclerView)
    }

    private fun loadAdapter(context: Context?, crashRecyclerView: RecyclerView?) {
        logAdapter = CrashLogAdapter(context!!, allCrashes)
        crashRecyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        crashRecyclerView.adapter = logAdapter
    }

    fun clearLog() {
        if (logAdapter != null) {
            logAdapter!!.updateList(allCrashes)
        }
    }

    private val allCrashes: ArrayList<File>
        get() {
            val directoryPath: String?
            val crashReportPath = crashReportPath
            directoryPath = if (TextUtils.isEmpty(crashReportPath)) {
                defaultPath
            } else {
                crashReportPath
            }
            val directory = File(directoryPath!!)
            if (!directory.exists() || !directory.isDirectory) {
                throw RuntimeException("The path provided doesn't exists : $directoryPath")
            }
            val listOfFiles = ArrayList(listOf(*directory.listFiles()))
            val iterator = listOfFiles.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().name.contains(Constants.EXCEPTION_SUFFIX)) {
                    iterator.remove()
                }
            }
            Collections.sort(listOfFiles, Collections.reverseOrder<Any>())
            return listOfFiles
        }
}