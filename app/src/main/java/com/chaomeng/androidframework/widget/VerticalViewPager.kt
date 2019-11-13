package com.chaomeng.androidframework.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class VerticalViewPager : ViewPager {

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    private fun swapTouchEvent(event: MotionEvent?): MotionEvent? {
        event?.let {
            val swappedX = event.y /height * width
            val swappedY = event.x/width * height
            event.setLocation(swappedX, swappedY)
        }
        return event
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val handled = super.onInterceptTouchEvent(swapTouchEvent(ev))
        swapTouchEvent(ev)
        return handled
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val handled = super.onTouchEvent(swapTouchEvent(ev))
        swapTouchEvent(ev)
        return handled
    }
}