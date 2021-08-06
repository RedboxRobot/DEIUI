package com.delicloud.app.uidemo.fragment

import com.delicloud.app.uidemo.activity.DisplayDetailActivity
import com.delicloud.app.uidemo.R
import com.delicloud.app.uidemo.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_diaplay.*

/**
 * 布局页面
 * @author wangchangwei
 * @create 2019/7/15 15:15
 * @Describe
 */
class DisplayFragment : BaseFragment() {
    override fun layoutId(): Int = R.layout.fragment_diaplay

    override fun initView() {
        card_btn.setOnClickListener { navDetailActivity(DisplayDetailActivity.REQUEST_CODE_CARD) }
        notice_btn.setOnClickListener { navDetailActivity(DisplayDetailActivity.REQUEST_CODE_NOTICE) }
        annunciate_btn.setOnClickListener { navDetailActivity(DisplayDetailActivity.REQUEST_CODE_ANNUNCIATE) }
        step_btn.setOnClickListener { navDetailActivity(DisplayDetailActivity.REQUEST_CODE_STEP) }
        progress_btn.setOnClickListener { navDetailActivity(DisplayDetailActivity.REQUEST_CODE_PROGRESS) }
    }

    private fun navDetailActivity(code: Int) {
        DisplayDetailActivity.start(context!!, code)
    }

    override fun initData() {
    }

}