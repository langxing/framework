package com.chaomeng.androidframework.ui.collect

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.adapter.BaseAdapter
import com.chaomeng.androidframework.base.BaseActivity
import com.chaomeng.androidframework.bean.ProjectBean
import com.chaomeng.androidframework.common.ViewModelFactory
import com.chaomeng.androidframework.utils.ImageLoader
import kotlinx.android.synthetic.main.activity_collect.*
import java.text.SimpleDateFormat
import java.util.*

class CollectActivity : BaseActivity() {

    private val projectList = ObservableArrayList<ProjectBean.Project>()

    private val model: CollectViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this)).get(CollectViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        model.queryCollect()
        model.projectList.observe(this,
            Observer<List<ProjectBean.Project>> {
                projectList.clear()
                projectList.addAll(it)
            })
        recyclerview.adapter = Adapter(projectList)
        recyclerview.layoutManager = LinearLayoutManager(this)
    }

    class Adapter(private val list: ObservableList<ProjectBean.Project>) : BaseAdapter<ProjectBean.Project>(list) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_project_item, parent, false)
            return Holder(view)
        }

        @SuppressLint("SimpleDateFormat")
        override fun onBindViewHolder(holder: Holder, position: Int) {
            val item = list[position]
            val imageView = holder.getView<ImageView>(R.id.imageview)
            val time = SimpleDateFormat("yyyy-MM-dd").format(Date(item.publishTime.toLong()))
            holder.setText(R.id.tvTitle, item.title)
                .setText(R.id.tvDesc, item.desc)
                .setText(R.id.tvAuthor, item.author)
                .setText(R.id.tvTime, time)
            ImageLoader.get()
                .with(imageView)
                .load(item.envelopePic)
                .defaultOptions()
                .into(imageView)
        }

    }
}