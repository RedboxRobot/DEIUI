package com.delicloud.app.uidemo.fragment


import android.content.DialogInterface
import android.widget.ImageView
import com.delicloud.app.deiui.feedback.notice.DeiUiProgressUtil
import com.delicloud.app.uidemo.R
import com.delicloud.app.uidemo.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_feedback_notice.*

/**
 * 轻提示界面
 */
class FeedbackNoticeFragment : BaseFragment() {


    override fun layoutId(): Int {
        return R.layout.fragment_feedback_notice
    }

    override fun initView() {
        loading_rect_btn.setOnClickListener {
            DeiUiProgressUtil.displaySpecialProgress(
                context,
                childFragmentManager,
                true,
                DialogInterface.OnCancelListener { })
        }

        success_btn.setOnClickListener {
            //成功轻提示
            val imageView = ImageView(context)
            imageView.setImageResource(R.mipmap.deiui_ic_progress_done)
            DeiUiProgressUtil.displayProgressAutoDismiss(
                context!!,
                childFragmentManager,
                "XXXXXXXXX成功",
                "2".toDouble(),
                imageView
            )
        }
        error_btn.setOnClickListener {
            //错误轻提示
            val imageView = ImageView(context)
            imageView.setImageResource(R.mipmap.deiui_ic_progress_error)
            DeiUiProgressUtil.displayProgressAutoDismiss(
                context!!,
                childFragmentManager,
                "XXXXXXX错误",
                "2".toDouble(),
                imageView
            )
        }
        warning_btn.setOnClickListener {
            //警告轻提示
            val imageView = ImageView(context)
            imageView.setImageResource(R.mipmap.deiui_ic_progress_warning)
            DeiUiProgressUtil.displayProgressAutoDismiss(
                context!!, childFragmentManager, "请输入会议号和会议密码\n" +
                        "和会议账号手机账号", "3".toDouble(), imageView
            )
        }
        text_btn.setOnClickListener {
            //只含文本轻提示
            DeiUiProgressUtil.displayTextAutoDismiss(
                context,
                childFragmentManager,
                "请输入会议号和会议密码",
                "3".toDouble()
            )
        }
    }

    override fun initData() {

    }
}
