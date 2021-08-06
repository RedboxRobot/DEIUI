package com.delicloud.app.uidemo.fragment

import com.delicloud.app.uidemo.activity.NavigationActivity
import com.delicloud.app.uidemo.R
import com.delicloud.app.uidemo.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_navigation.*
import org.jetbrains.anko.support.v4.act

/**
 * 导航界面
 * @author wangchangwei
 * @create 2019/7/15 15:15
 * @Describe
 */
class NavigationFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_navigation

    override fun initView() {
        nav_toolbar_single_title_btn.setOnClickListener { navNavigationActivity(NavigationActivity.NAVIGATION_SINGLE_TITLE) }
        nav_toolbar_double_title_btn.setOnClickListener { navNavigationActivity(NavigationActivity.NAVIGATION_DOUBLE_TITLE) }
        nav_toolbar_right_top_menu_float.setOnClickListener { navNavigationActivity(NavigationActivity.NAVIGATION_RIGHT_TOP_POPUP_MENU_TEXT) }
        nav_toolbar_right_top_menu_multi_choice.setOnClickListener { navNavigationActivity(NavigationActivity.NAVIGATION_RIGHT_TOP_POPUP_MENU_IMG_TEXT) }
    }

    override fun initData() {

    }

    private fun navNavigationActivity(code: Int) {
        NavigationActivity.start(act, code)
    }

}
