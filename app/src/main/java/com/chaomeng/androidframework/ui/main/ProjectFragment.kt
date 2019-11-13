package com.chaomeng.androidframework.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chaomeng.androidframework.R
import com.chaomeng.androidframework.bean.Category
import com.chaomeng.androidframework.common.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_project.*

class ProjectFragment : Fragment() {

    private val model: MainViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(this)).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.queryCategory()
        model.categoryList.observe(this, Observer<List<Category>> {
//            it.forEachIndexed { index, category ->
//                tablayout.addTab(tablayout.newTab().setText(category.name).setTag(index))
//            }
            viewpager.adapter!!.notifyDataSetChanged()
            viewpager.offscreenPageLimit = model.categoryList.value!!.size
        })
        viewpager.adapter = object: FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            override fun getItem(position: Int): Fragment {
                return ProjectItemFragment.create(ProjectItemFragment.KEY_ID to "${model.categoryList.value?.get(position)?.id}")
            }

            override fun getCount(): Int {
                return model.categoryList.value?.size ?: 0
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return model.categoryList.value?.get(position)?.name
            }
        }
        viewpager.currentItem = 0
        tablayout.setupWithViewPager(viewpager)
    }
}