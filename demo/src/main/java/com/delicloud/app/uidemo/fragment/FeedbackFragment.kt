package com.delicloud.app.uidemo.fragment

import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.delicloud.app.uidemo.R
import com.delicloud.app.uidemo.adapter.SimpleFragmentPagerAdapter
import com.delicloud.app.uidemo.base.BaseFragment
import com.google.android.material.tabs.TabLayout


/**
 * 布局页面
 * @author wangchangwei
 * @create 2019/7/15 15:15
 * @Describe
 */
class FeedbackFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_feedback

    override fun initView() {
        view?.findViewById<ViewPager>(R.id.feedback_vp)?.let {
           it.adapter= SimpleFragmentPagerAdapter(
                activity!!.supportFragmentManager, arrayListOf(FeedbackDialogFragment(), FeedbackNoticeFragment()),
                arrayListOf("弹窗", "轻提示")
            )
            view?.findViewById<TabLayout>(R.id.feedback_tabLayout)?.setupWithViewPager(it)
        }
    }


    override
    fun initData() {
    }
}