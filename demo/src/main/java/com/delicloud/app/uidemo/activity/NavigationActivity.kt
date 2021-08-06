package com.delicloud.app.uidemo.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.delicloud.app.uidemo.adapter.SimpleFragmentPagerAdapter
import com.delicloud.app.uidemo.extentions.contentView
import com.delicloud.app.uidemo.fragment.EmptyFragment
import com.delicloud.app.uidemo.utils.getStatusBarHeight
import com.delicloud.app.uidemo.utils.setDarkStatusIcon
import com.delicloud.app.uidemo.utils.setStatusTransAndDarkIcon
import com.delicloud.app.deiui.navigation.model.DeiUiMenuPopupWindowModel
import com.delicloud.app.deiui.navigation.widget.DeiUiMenuPopupWindow
import com.delicloud.app.uidemo.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_navigation.*
import org.jetbrains.anko.textColor
import org.jetbrains.anko.topPadding
import java.util.*

class NavigationActivity : AppCompatActivity() {
    private val popupItemTextIds = arrayOf(
        R.string.popup_window_item_1,
        R.string.popup_window_item_2,
        R.string.popup_window_item_3,
        R.string.popup_window_item_4,
        R.string.popup_window_item_5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        initView()
        initData()
    }

    fun initView() {
        setStatusTransAndDarkIcon(Color.WHITE)
        contentView?.topPadding = getStatusBarHeight()
        val singleTitleToolbar = findViewById<Toolbar>(R.id.single_title_toolbar)
        val navigationDoubleTitleToolbar =
            findViewById<Toolbar>(R.id.navigation_double_title_toolbar)
        val navigationDoubleTitleTabLayout =
            findViewById<TabLayout>(R.id.navigation_double_title_tabLayout)
        singleTitleToolbar.setNavigationOnClickListener { finish() }
        when (navigationCode) {
            NAVIGATION_SINGLE_TITLE, NAVIGATION_RIGHT_TOP_POPUP_MENU_TEXT, NAVIGATION_RIGHT_TOP_POPUP_MENU_IMG_TEXT -> {
                singleTitleToolbar.visibility = View.VISIBLE
                navigationDoubleTitleToolbar.visibility = View.INVISIBLE
                if (navigationCode != NAVIGATION_SINGLE_TITLE) {
                    setStatusTransAndDarkIcon(
                        ContextCompat.getColor(
                            this,
                            R.color.deiui_main_color
                        )
                    )
                    setDarkStatusIcon(false)
                    singleTitleToolbar.apply {
                        navigationIcon = null
                        singleTitleToolbar.inflateMenu(R.menu.menu_item_plus)
                        background = ColorDrawable(
                            ContextCompat.getColor(
                                this@NavigationActivity,
                                R.color.deiui_main_color
                            )
                        )
                    }
                    //蓝色主题背景标题设置白色
                    findViewById<TextView>(R.id.toolbar_title_tv).apply { textColor = Color.WHITE }
                } else setSupportActionBar(singleTitleToolbar)
                singleTitleToolbar.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_add -> {
                            if (navigationCode == NAVIGATION_RIGHT_TOP_POPUP_MENU_IMG_TEXT)
                                initPopupWindow(
                                    singleTitleToolbar,
                                    DeiUiMenuPopupWindow.PopItemTheme.IMG_TEXT
                                )
                            if (navigationCode == NAVIGATION_RIGHT_TOP_POPUP_MENU_TEXT) {
                                initPopupWindow(
                                    singleTitleToolbar,
                                    DeiUiMenuPopupWindow.PopItemTheme.TEXT
                                )
                            }
                        }
                    }

                    return@setOnMenuItemClickListener true
                }
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
            NAVIGATION_DOUBLE_TITLE -> {
                singleTitleToolbar.visibility = View.INVISIBLE
                navigationDoubleTitleToolbar.visibility = View.VISIBLE
                setSupportActionBar(navigationDoubleTitleToolbar)
                supportActionBar?.setDisplayShowTitleEnabled(false)
                val fragments = listOf<Fragment>(EmptyFragment("个人"), EmptyFragment("企业"))
                double_title_vp.adapter = SimpleFragmentPagerAdapter(
                    supportFragmentManager,
                    fragments,
                    arrayListOf("个人", "企业")
                )
                double_title_vp.visibility = View.VISIBLE
                navigationDoubleTitleTabLayout.setupWithViewPager(double_title_vp)
                navigationDoubleTitleToolbar.setNavigationOnClickListener { finish() }
            }
        }

    }

    private fun initData() {

    }

    private fun initPopupWindow(toolbar: Toolbar, theme: DeiUiMenuPopupWindow.PopItemTheme) {
        val list = ArrayList<DeiUiMenuPopupWindowModel>()
        var popupEntity: DeiUiMenuPopupWindowModel
        for (i in 0 until popupItemTextIds.size) {
            popupEntity = DeiUiMenuPopupWindowModel()
            if (theme == DeiUiMenuPopupWindow.PopItemTheme.IMG_TEXT)
                popupEntity.imageResource = R.drawable.ic_nav_mult
            popupEntity.textInt = popupItemTextIds[i]
            popupEntity.textColor = ContextCompat.getColor(this,
                R.color.colorAccent
            )
            list.add(popupEntity)
            if (i == 2 && theme == DeiUiMenuPopupWindow.PopItemTheme.TEXT)
                break
        }
        val mMenuPopupWindow = DeiUiMenuPopupWindow(this, list, theme)
        mMenuPopupWindow.showPopupWindowAsDefault(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when (navigationCode) {
            NAVIGATION_SINGLE_TITLE -> {
                menuInflater.inflate(R.menu.top_right_double_img, menu)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val NAVIGATION_SINGLE_TITLE = 0x01
        const val NAVIGATION_DOUBLE_TITLE = 0x02
        const val NAVIGATION_RIGHT_TOP_POPUP_MENU_TEXT = 0X03
        const val NAVIGATION_RIGHT_TOP_POPUP_MENU_IMG_TEXT = 0x04
        var navigationCode = 0x01
        fun start(activity: Activity, code: Int) {
            activity.startActivity(Intent(activity, NavigationActivity::class.java))
            navigationCode = code
        }
    }

}
