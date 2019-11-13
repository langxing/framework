package com.chaomeng.androidframework.widget

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

class SmoothScroller(context: Context) : LinearSmoothScroller(context){

    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
}