package com.chaomeng.androidframework.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.adapter.BaseAdapter
import com.chaomeng.androidframework.bean.ArticleBean
import com.chaomeng.androidframework.bean.Banner
import com.chaomeng.androidframework.common.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private var mPageIndex = 0
    private var articleList = ObservableArrayList<ArticleBean.Article>()
    private val model: MainViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this)).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.queryData()
        model.queryBanner()
        recyclerview.adapter = Adapter(articleList)
        recyclerview.layoutManager = LinearLayoutManager(context)
        model.bannerList.observe(this, Observer<List<Banner>> {
            xbanner.setBannerData(it)
        })
        model.articleList.observe(this, Observer<List<ArticleBean.Article>> {
            if (mPageIndex == 0) {
                articleList.clear()
                refreshLayout.finishRefresh()
            } else {
                refreshLayout.finishLoadMore()
            }
            articleList.addAll(it)
            mPageIndex ++
        })
        refreshLayout.setOnRefreshListener {
            mPageIndex = 0
            model.queryData()
        }
        refreshLayout.setOnLoadMoreListener {
            model.queryData(mPageIndex)
        }
    }

    class Adapter(private val list: ObservableList<ArticleBean.Article>) : BaseAdapter<ArticleBean.Article>(list) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_article_item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val item = list[position]
            holder.setText(R.id.tvSuperChapter, item.superChapterName)
                .setText(R.id.tvChapter, item.chapterName)
                .setText(R.id.tvTime, item.niceDate)
                .setText(R.id.tvTitle, item.title)
        }

    }
}