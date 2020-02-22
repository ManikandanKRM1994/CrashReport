package com.krm.crashreport.utils

import androidx.viewpager.widget.ViewPager.OnPageChangeListener

abstract class SimplePageChangeListener : OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    abstract override fun onPageSelected(position: Int)
    override fun onPageScrollStateChanged(state: Int) {}
}