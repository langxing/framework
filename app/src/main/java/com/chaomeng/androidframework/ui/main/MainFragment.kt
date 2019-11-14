package com.chaomeng.androidframework.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.adapter.BaseAdapter
import com.chaomeng.androidframework.bean.ArticleBean
import com.chaomeng.androidframework.bean.Banner
import com.chaomeng.androidframework.bean.LoginInfo
import com.chaomeng.androidframework.common.Constant
import com.chaomeng.androidframework.common.ViewModelFactory
import com.chaomeng.androidframework.ui.collect.CollectActivity
import com.chaomeng.androidframework.ui.login.LoginActivity
import com.chaomeng.androidframework.utils.CacheManager
import com.chaomeng.androidframework.utils.ImageLoader
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private var mPageIndex = 0
    private var cookie: String? = null
    private var loginInfo: LoginInfo? = null
    private var articleList = ObservableArrayList<ArticleBean.Article>()

    private var accountTv: TextView? = null

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

    override fun onResume() {
        super.onResume()
        cookie = CacheManager.get().getString(Constant.KEY_COOKIE)
        loginInfo = CacheManager.get().getObject<LoginInfo>(Constant.KEY_LOGININFO)
        accountTv?.text = if (cookie.isNullOrEmpty()) "未登录" else loginInfo?.publicName
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.queryData()
        model.queryBanner()
        recyclerview.adapter = Adapter(articleList)
        recyclerview.layoutManager = LinearLayoutManager(context)
        banner.setImageLoader(object: com.youth.banner.loader.ImageLoader() {
            override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                (path as Banner?)?.let {
                    ImageLoader.get()
                        .with(this@MainFragment)
                        .load(it.imagePath)
                        .defaultOptions()
                        .into(imageView!!)
                }
            }

        })
        model.bannerList.observe(this, Observer<List<Banner>> {
            banner.setImages(it).start()
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
        model.isLogout.observe(this, Observer<Boolean> {
            if (it) {
                accountTv?.text = "未登录"
            }
        })
        refreshLayout.setOnRefreshListener {
            mPageIndex = 0
            model.queryData()
        }
        refreshLayout.setOnLoadMoreListener {
            model.queryData(mPageIndex)
        }
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tvCollect -> {
                    val intent = if (cookie.isNullOrEmpty()) {
                        Intent(activity, LoginActivity::class.java)
                    } else {
                        Intent(activity, CollectActivity::class.java)
                    }
                    startActivity(intent)
                }
                R.id.tvLogout -> {
                    model.logout()
                }
            }
            return@setNavigationItemSelectedListener true
        }
        accountTv = navigationView.getHeaderView(0).findViewById(R.id.tvAccount)
        accountTv?.setOnClickListener {
            cookie = CacheManager.get().getString(Constant.KEY_COOKIE)
            if (cookie.isNullOrEmpty()) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
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