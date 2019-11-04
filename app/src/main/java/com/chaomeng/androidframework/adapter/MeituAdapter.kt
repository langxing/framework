package com.chaomeng.androidframework.adapter

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.chaomeng.androidframework.bean.Meitu

class MeituAdapter(private val data: ObservableList<Meitu>) : BaseAdapter() {

    init {
        data.addOnListChangedCallback(object: ObservableList.OnListChangedCallback<ObservableList<Meitu>>() {
            override fun onChanged(sender: ObservableList<Meitu>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(
                sender: ObservableList<Meitu>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(
                sender: ObservableList<Meitu>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeInserted(
                sender: ObservableList<Meitu>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(
                sender: ObservableList<Meitu>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val textView = TextView(parent.context)
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 150)
        textView.layoutParams= layoutParams
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.parseColor("#16B0DC"))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        textView.maxLines = 1
        return Holder(textView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        (holder.itemview as TextView).text = data[position].toString()
    }
}