package com.delicloud.app.uidemo.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.delicloud.app.uidemo.R
import com.delicloud.app.uidemo.extentions.contentView
import com.delicloud.app.uidemo.extentions.setupWithViewPager
import com.delicloud.app.uidemo.fragment.DisplayFragment
import com.delicloud.app.uidemo.fragment.EntryFragment
import com.delicloud.app.uidemo.fragment.FeedbackFragment
import com.delicloud.app.uidemo.fragment.NavigationFragment
import com.delicloud.app.uidemo.utils.getStatusBarHeight
import com.delicloud.app.uidemo.utils.setDarkStatusIcon
import com.delicloud.app.uidemo.utils.setStatusTransAndDarkIcon
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.textColor
import org.jetbrains.anko.topPadding

/**
 * 主页
 */
class MainActivity : AppCompatActivity() {
    private val tabTitles = arrayOf("导航", "数据录入", "数据展示", "操作反馈")
    private lateinit var titleTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

    }

    fun initView() {
        setStatusTransAndDarkIcon(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.deiui_main_color
            )
        )
        setDarkStatusIcon(false)
        contentView?.topPadding = getStatusBarHeight()
        bottom_nav.setItemIconTintList(null)
        view_pager.adapter = MainPagerAdapter(supportFragmentManager)
        view_pager.offscreenPageLimit = 4
        bottom_nav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        bottom_nav.setupWithViewPager(view_pager)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                titleTv.text = tabTitles.get(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })
        findViewById<Toolbar>(R.id.single_title_toolbar).apply {
            navigationIcon = null
            background =
                ColorDrawable(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.deiui_main_color
                    )
                )
            inflateMenu(R.menu.menu_item_version)
            setOnMenuItemClickListener {
                startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                return@setOnMenuItemClickListener true
            }

        }
        titleTv = findViewById<TextView>(R.id.toolbar_title_tv).apply {
            text = "导航"
            textColor = Color.WHITE
        }
    }


    inner class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {

            return when (position) {
//                0 -> LayoutFragment()
//                1 -> NavigationFragment()
//                2 -> EntryFragment()
//                3 -> DisplayFragment()
//                4 -> FeedbackFragment()
//                else -> LayoutFragment()
                0 -> NavigationFragment()
                1 -> EntryFragment()
                2 -> DisplayFragment()
                3 -> FeedbackFragment()
                else -> NavigationFragment()
            }
        }

        override fun getCount() = 5
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }


}
