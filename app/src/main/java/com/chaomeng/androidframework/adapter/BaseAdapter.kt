package com.chaomeng.androidframework.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(val data: ObservableList<T>) : RecyclerView.Adapter<BaseAdapter.Holder>() {

    init {
        data.addOnListChangedCallback(object: ObservableList.OnListChangedCallback<ObservableList<T>>() {
            override fun onChanged(sender: ObservableList<T>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(
                sender: ObservableList<T>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(
                sender: ObservableList<T>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeInserted(
                sender: ObservableList<T>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(
                sender: ObservableList<T>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class Holder(val itemview: View) : RecyclerView.ViewHolder(itemview) {

        fun <V : View> getView(id: Int): V {
            return itemview.findViewById(id)
        }

        fun setText(id: Int, text: String): Holder {
            getView<TextView>(id).text = text
            return this
        }
    }
}