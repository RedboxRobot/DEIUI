package com.delicloud.app.uidemo.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.delicloud.app.uidemo.R
import com.delicloud.app.uidemo.extentions.contentView
import com.delicloud.app.uidemo.utils.PackageUtils
import com.delicloud.app.uidemo.utils.getStatusBarHeight
import com.delicloud.app.uidemo.utils.setDarkStatusIcon
import com.delicloud.app.uidemo.utils.setStatusTransAndDarkIcon
import kotlinx.android.synthetic.main.activity_version.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import org.jetbrains.anko.topPadding

class AboutActivity : AppCompatActivity() {
    private lateinit var mToolbar: Toolbar
    private lateinit var mTitleTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_version)
        contentView?.topPadding = getStatusBarHeight()
        setStatusTransAndDarkIcon(ContextCompat.getColor(this, R.color.deiui_main_color))
        setDarkStatusIcon(false)
        initView()


    }

    fun initView() {
        mToolbar = findViewById<Toolbar>(R.id.single_title_toolbar)
        mToolbar.apply {
            backgroundColor = ContextCompat.getColor(context, R.color.deiui_main_color)
            setNavigationIcon(R.drawable.ic_back_white)
            setNavigationOnClickListener {
                finish()
            }
        }
        mTitleTv = mToolbar.findViewById(R.id.toolbar_title_tv)
        mTitleTv.text = "关于"
        mTitleTv.textColor = Color.WHITE
        version_tv.text="版本 ${PackageUtils.getVersionName(this)}"
    }
}
