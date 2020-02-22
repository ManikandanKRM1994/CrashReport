package com.krm.crashreport.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.krm.crashreport.ui.CrashLogFragment
import com.krm.crashreport.ui.ExceptionLogFragment

class MainPagerAdapter(fm: FragmentManager?, private val titles: Array<String>) : FragmentPagerAdapter(fm!!) {
    private var crashLogFragment: CrashLogFragment? = null
    private var exceptionLogFragment: ExceptionLogFragment? = null
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CrashLogFragment().also { crashLogFragment = it }
            }
            1 -> {
                ExceptionLogFragment().also { exceptionLogFragment = it }
            }
            else -> {
                CrashLogFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    fun clearLogs() {
        crashLogFragment!!.clearLog()
        exceptionLogFragment!!.clearLog()
    }

}