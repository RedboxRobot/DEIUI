package com.delicloud.app.uidemo.fragment

import com.delicloud.app.uidemo.activity.EntryDetailActivity
import com.delicloud.app.uidemo.R
import com.delicloud.app.uidemo.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_entry.*

/**
 * 布局页面
 * @author wangchangwei
 * @create 2019/7/15 15:15
 * @Describe
 */
class EntryFragment : BaseFragment() {
    override fun layoutId(): Int = R.layout.fragment_entry

    override fun initView() {
        search_btn.setOnClickListener { navEntryDetailActivity(EntryDetailActivity.REQUEST_CODE_SEARCH_NORMAL) }
        btn.setOnClickListener { navEntryDetailActivity(EntryDetailActivity.REQUEST_CODE_BUTTON) }
        list_btn.setOnClickListener { navEntryDetailActivity(EntryDetailActivity.REQUEST_CODE_LIST) }
        select_btn.setOnClickListener { navEntryDetailActivity(EntryDetailActivity.REQUEST_CODE_SELECT) }
        operate_btn.setOnClickListener { navEntryDetailActivity(EntryDetailActivity.REQUEST_CODE_OPERATE) }
        share_btn.setOnClickListener { navEntryDetailActivity(EntryDetailActivity.REQUEST_CODE_SHARE) }
        form_btn.setOnClickListener { navEntryDetailActivity(EntryDetailActivity.REQUEST_CODE_FORM) }
    }



    override fun initData() {
    }

    private fun navEntryDetailActivity(code: Int) {
        EntryDetailActivity.start(context!!, code)
    }


}