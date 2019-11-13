package com.chaomeng.androidframework.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.adapter.ViewPagerAdapter
import com.chaomeng.androidframework.base.BaseActivity
import com.chaomeng.androidframework.databinding.ActivityMainBinding

@SuppressLint("Registered")
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databing = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        databing.viewpager.adapter = ViewPagerAdapter(supportFragmentManager, arrayOf(MainFragment(), ProjectFragment(), SystemFragment(), NavigationFragment()))
        databing.tabview.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tabHome -> databing.viewpager.currentItem = 0
                R.id.tabProject -> databing.viewpager.currentItem = 1
                R.id.tabSystem -> databing.viewpager.currentItem = 2
                R.id.tabNavigation -> databing.viewpager.currentItem = 3
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}