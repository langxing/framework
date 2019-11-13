package com.chaomeng.androidframework.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, private val fragmentArray: Array<Fragment>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragmentArray[position]
    }

    override fun getCount(): Int {
        return fragmentArray.size
    }

}