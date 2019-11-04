package com.chaomeng.androidframework.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter : RecyclerView.Adapter<BaseAdapter.Holder>() {

    class Holder(val itemview: View) : RecyclerView.ViewHolder(itemview) {

        fun <V : View> getView(id: Int): V {
            return itemview.findViewById(id)
        }

        fun setText(id: Int, text: String) {
            getView<TextView>(id).text = text
        }
    }
}