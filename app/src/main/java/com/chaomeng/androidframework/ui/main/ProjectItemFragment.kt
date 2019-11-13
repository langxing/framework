package com.chaomeng.androidframework.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.adapter.BaseAdapter
import com.chaomeng.androidframework.bean.ProjectBean
import com.chaomeng.androidframework.common.ViewModelFactory
import com.chaomeng.androidframework.utils.ImageLoader
import kotlinx.android.synthetic.main.fragment_project_item.*
import java.text.SimpleDateFormat
import java.util.*

class ProjectItemFragment : Fragment() {

    private var pageIndex = 0
    private var firstLoad = true
    private val projectList = ObservableArrayList<ProjectBean.Project>()
    private lateinit var projectId: String

    private val model: MainViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this)).get(MainViewModel::class.java)
    }

    companion object {
        const val KEY_ID = "id"

        fun create(vararg params: Pair<String, Any>): ProjectItemFragment {
            val fragment = ProjectItemFragment()
            fragment.arguments = bundleOf(*params)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_project_item, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (!firstLoad && projectId.isNotEmpty()) {
            model.queryProject(projectId)
        } else {
            firstLoad = !firstLoad
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectId = arguments?.getString(KEY_ID) ?: "0"
        model.queryProject(projectId)
        model.projectList.observe(this, Observer<List<ProjectBean.Project>> {
            if (pageIndex == 0) {
                projectList.clear()
            }
            projectList.addAll(it)
        })
        recyclerview.adapter = Adapter(projectList)
        recyclerview.layoutManager = LinearLayoutManager(context)
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