package com.delicloud.app.uidemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.delicloud.app.uidemo.utils.setDarkStatusIcon
import com.delicloud.app.deiui.display.StepBean
import com.delicloud.app.deiui.utils.ScreenUtil
import com.delicloud.app.uidemo.R
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_display.*
import kotlinx.android.synthetic.main.display_announciate_layout.*
import kotlinx.android.synthetic.main.display_notice_layout.*
import kotlinx.android.synthetic.main.display_step_layout.*
import java.util.*

class DisplayDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        initView()
    }

    private fun initView() {
        setDarkStatusIcon(true)
        findViewById<Toolbar>(R.id.single_title_toolbar).setNavigationOnClickListener { finish() }
        findViewById<TextView>(R.id.toolbar_title_tv).text = "Display"
        when (code) {
            REQUEST_CODE_CARD -> {
                //卡片
                initCardLayout()
            }
            REQUEST_CODE_NOTICE -> {
                //标识提醒
                initNoticeLayout()
            }
            REQUEST_CODE_ANNUNCIATE -> {
                //通告栏
                initAnnunciateLayout()
            }
            REQUEST_CODE_STEP -> {
                //步骤条
                initStepLayout()
            }
            REQUEST_CODE_PROGRESS -> {
                initProgressLayout()
                //进度条
            }
        }
    }

    /**
     * 卡片
     */
    private fun initCardLayout() {
        card_layout.visibility = View.VISIBLE
        val cardBgDrawable =
            ContextCompat.getDrawable(this@DisplayDetailActivity,
                R.drawable.deiui_space_office_bg
            )
        card_layout.run {
            //单标题居中，无描述
            findViewById<ConstraintLayout>(R.id.cardview_center_title_constraint_layout).apply {
                background = cardBgDrawable
                findViewById<TextView>(R.id.card_title_tv).text = "得力办公"
            }
            //单标题
            findViewById<ConstraintLayout>(R.id.cardview_single_title_constraint_layout).apply {
                background = cardBgDrawable
                findViewById<TextView>(R.id.card_title_tv).text = "得力办公"
                findViewById<TextView>(R.id.card_desc_tv).text =
                    "构建得力智能办公大数据平台，采集得力云平台生态数据，为客户提供更好的办公服务。"
            }
            //双标题，分为主标题和子标题
            findViewById<ConstraintLayout>(R.id.cardview_double_title_constraint_layout).apply {
                background = cardBgDrawable
                findViewById<TextView>(R.id.card_title_tv).text = "得力办公"
                findViewById<TextView>(R.id.card_subtitle_tv).text =
                    "构建得力智能办公大数据平台，采集得力云平台生态数据，为客户提供更好的办公服务。"
            }
            //功能性卡片，纵向分布
            findViewById<ConstraintLayout>(R.id.card_function_constraintlayout_horizontal).apply {
                findViewById<ImageView>(R.id.card_function_iv).background =
                    ContextCompat.getDrawable(
                        this@DisplayDetailActivity,
                        R.drawable.ic_card_horization
                    )
                findViewById<TextView>(R.id.card_function_title_tv).text = "得力办公"
                findViewById<TextView>(R.id.card_function_desc_tv).text = "构建得力智能办公大数据平台，采集得力云平台"
            }
            //功能性卡片垂直分布
            findViewById<LinearLayout>(R.id.card_function_ll_vertical).apply {
                findViewById<ImageView>(R.id.card_function_iv).setImageResource(
                    R.drawable.ic_card_vertical
                )
                findViewById<TextView>(R.id.card_function_title_tv).text = "得力办公"
                findViewById<TextView>(R.id.card_function_desc_tv).text = "(文字信息)"
            }
        }
    }

    /**
     * 标识提醒布局，小红点仅提供图片，可根据需要进行选择，包括圆点，圆点带文字，以及圆角
     *
     */
    private fun initNoticeLayout() {
        notice_layout.visibility = View.VISIBLE
        val tagString = "构建得力智能办公大数据平台"
        //tag数据
        val tagData = listOf(
            tagString.substring(0, 1),
            tagString.substring(2, 4),
            tagString.substring(1, 5),
            tagString.substring(3, 6),
            tagString.substring(1, 5),
            tagString.substring(1, 8),
            tagString.substring(0, 2),
            tagString.substring(0, 1),
            tagString.substring(2, 5)
        )
        //设置Adapter
        tagflowlayout.adapter = object : TagAdapter<String>(tagData) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                //返回item 布局
                return (LayoutInflater.from(this@DisplayDetailActivity).inflate(
                    R.layout.deiui_item_tag,
                    parent,
                    false
                ) as TextView).apply {
                    text = t
                }
            }
        }
        red_point_1.setPointRadius(ScreenUtil.dip2px(5f))
        red_point_2.setNotifyValue(3)
        red_point_3.setNotifyValue(56)
        notify_more.setNotifyValue(100)

    }

    /**
     * 公告
     */
    private fun initAnnunciateLayout() {
        announciate_layout.visibility = View.VISIBLE
        marquee_prize.setContent("1000元红包等你来抽！1000元红包等你来抽！1000元红包等你来抽！1000元红包等你来抽！1000元红包等你来抽！1000元红包等你来抽！")
        marquee_warn.setContent("1000元红包等你来抽！1000元红包等你来抽！")
    }

    private fun initStepLayout() {
        step_layout.visibility = View.VISIBLE
        step_view
            //set textSize
            .setTextSize(13)
            //设置StepsViewIndicator 完成线的图片
            .setStepsViewIndicatorCompleteLine(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.deiui_transition2
                )!!
            )
            //设置StepsViewIndicator 当前线的图片
            .setStepsViewIndicatorAttentionLine(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.deiui_transition1
                )!!
            )
            //设置StepsViewIndicator 默认线的图片
            .setStepsViewIndicatorDefaultLine(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.deiui_transition0
                )!!
            )
            //设置StepsView text完成文字的颜色
            .setStepViewComplectedTextColor(ContextCompat.getColor(this, android.R.color.white))
            //设置StepsView text未完成文字的颜色
            .setStepViewUnComplectedTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.deiui_uncompleted_text_color
                )
            )
            //设置StepsViewIndicator 完成图标
            .setStepsViewIndicatorCompleteIcon(
                ContextCompat.getDrawable(
                    this,
                    R.mipmap.ic_step_complted
                )!!
            )
            //设置StepsViewIndicator 进行中图标
            .setStepsViewIndicatorAttentionIcon(
                ContextCompat.getDrawable(
                    this,
                    R.mipmap.ic_step_complted
                )!!
            )
            //设置StepsViewIndicator 默认图标
            .setStepsViewIndicatorDefaultIcon(
                ContextCompat.getDrawable(
                    this,
                    R.mipmap.ic_step_default
                )!!
            )


        //步骤列表
        val stepsBeanList = ArrayList<StepBean>()
        val stepBean0 = StepBean("发现设备", StepBean.STEP_CURRENT, 0)
        val stepBean1 = StepBean("连接设备", StepBean.STEP_UNDO, 1)
        val stepBean2 = StepBean("添加完成", StepBean.STEP_UNDO, 2)
        stepsBeanList.add(stepBean0)
        stepsBeanList.add(stepBean1)
        stepsBeanList.add(stepBean2)

        //设置步骤
        step_view.setStepViewTexts(stepsBeanList)
    }

    private fun initProgressLayout() {
        progress_layout.visibility = View.VISIBLE
    }


    companion object {
        const val REQUEST_CODE_CARD = 0x01
        const val REQUEST_CODE_NOTICE = 0x02
        const val REQUEST_CODE_ANNUNCIATE = 0x03
        const val REQUEST_CODE_STEP = 0x04
        const val REQUEST_CODE_PROGRESS = 0x05
        private var code: Int = 0x01
        fun start(context: Context, code: Int) {
            Companion.code = code
            context.startActivity(Intent(context, DisplayDetailActivity::class.java))
        }
    }
}
