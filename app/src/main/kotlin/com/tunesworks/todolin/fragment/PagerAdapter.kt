package com.tunesworks.todolin.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.tunesworks.todolin.value.ItemColor

class PagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? = ListFragment.newInstance()

    override fun getPageTitle(position: Int): CharSequence? = ItemColor.values()[position].toString()

    override fun getCount(): Int = ItemColor.values().size
}