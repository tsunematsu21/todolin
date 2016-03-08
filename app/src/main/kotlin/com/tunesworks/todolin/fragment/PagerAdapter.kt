package com.tunesworks.todolin.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.tunesworks.todolin.value.ItemColor
import java.util.*

class PagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    val list = arrayOf("Priority", "Someday", "Done")

    override fun getItem(position: Int): Fragment? = ListFragment.newInstance()

    override fun getPageTitle(position: Int) = list[position]

    override fun getCount(): Int = list.size
}