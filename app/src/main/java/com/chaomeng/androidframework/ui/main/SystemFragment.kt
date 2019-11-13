package com.chaomeng.androidframework.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.adapter.BaseAdapter
import com.chaomeng.androidframework.bean.SystemBean
import com.chaomeng.androidframework.common.ViewModelFactory
import com.chaomeng.androidframework.widget.SmoothScroller
import kotlinx.android.synthetic.main.fragment_system.*

class SystemFragment : Fragment() {

    private var oldView: RadioButton? = null
    private val systemList = ObservableArrayList<SystemBean>()
    private val model: MainViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this)).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_system, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.querySystem()
        model.systemList.observe(this, Observer<List<SystemBean>> {
            systemList.clear()
            systemList.addAll(it)
        })
        radiogroup.adapter = object: BaseAdapter<SystemBean>(systemList) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
                val view = LayoutInflater.from(context).inflate(R.layout.layout_system_radioitem, parent, false)
                view.id = View.generateViewId()
                return Holder(view)
            }

            override fun onBindViewHolder(holder: Holder, position: Int) {
                val item = systemList[position]
                val radioButton = holder.itemview as RadioButton
                radioButton.text = item.name
                radioButton.tag = item.id
                holder.itemview.setOnClickListener {
                    oldView?.isChecked = false
                    radioButton.isChecked = true
                    oldView = radioButton
                    val smoothScroller = SmoothScroller(context!!)
                    smoothScroller.targetPosition = position
                    recyclerview.layoutManager?.startSmoothScroll(smoothScroller)
                }
            }

        }
        radiogroup.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = Adapter(systemList)
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                Log.e("position", "position:$position")
                for (i in 0 until radiogroup.adapter!!.itemCount) {
                    (radiogroup.layoutManager?.findViewByPosition(i) as RadioButton?)?.isChecked = i == position
                }
                radiogroup.smoothScrollToPosition(position)
            }
        })
    }

    inner class Adapter(private val list: ObservableList<SystemBean>) : BaseAdapter<SystemBean>(list) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val itemview = LayoutInflater.from(parent.context).inflate(R.layout.layout_system_item, parent, false)
            return Holder(itemview)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val item = list[position]
            val rootview = holder.getView<LinearLayout>(R.id.rootview)
            rootview.removeAllViews()
            item.children.forEachIndexed { index, systemItem ->
                val textView = TextView(context)
                val layoutparams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                if (index > 0) {
                    layoutparams.topMargin = 20
                }
                textView.setPadding(10, 10, 10, 10)
                textView.background = context?.resources?.getDrawable(R.drawable.shape_system_item)
                textView.layoutParams = layoutparams
                textView.gravity = Gravity.CENTER
                textView.text = systemItem.name
                textView.setTextColor(Color.parseColor("#4269E0"))
                rootview.addView(textView)
                textView.setOnClickListener {
                    // TODO
                }
            }
        }

    }
}