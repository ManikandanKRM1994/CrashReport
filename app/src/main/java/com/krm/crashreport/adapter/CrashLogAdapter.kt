package com.krm.crashreport.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krm.crashreport.ui.LogMessageActivity
import com.krm.crashreport.utils.FileUtils.readFirstLineFromFile
import com.krm.crashreport.R
import java.io.File
import java.util.*

class CrashLogAdapter(private val context: Context, private var crashFileList: ArrayList<File>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_item, null)
        return CrashLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CrashLogViewHolder).setUpViewHolder(context, crashFileList[position])
    }

    override fun getItemCount(): Int {
        return crashFileList.size
    }

    fun updateList(allCrashLogs: ArrayList<File>) {
        crashFileList = allCrashLogs
        notifyDataSetChanged()
    }

    private inner class CrashLogViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewMsg: TextView = itemView.findViewById<View>(R.id.textViewMsg) as TextView
        private val messageLogTime: TextView = itemView.findViewById<View>(R.id.messageLogTime) as TextView
        fun setUpViewHolder(context: Context, file: File) {
            val filePath = file.absolutePath
            messageLogTime.text = file.name.replace("[a-zA-Z_.]".toRegex(), "")
            textViewMsg.text = readFirstLineFromFile(File(filePath))
            textViewMsg.setOnClickListener {
                val intent = Intent(context, LogMessageActivity::class.java)
                intent.putExtra("LogMessage", filePath)
                context.startActivity(intent)
            }
        }

    }

}