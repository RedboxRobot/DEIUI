package com.delicloud.app.deiui.feedback.dialog

import android.content.Context

/**
 * @author ChengXinPing
 * @time 2018/1/4 15:47
 * 自定义dialog  只需要传入自定义布局和自定义style
 */

class DeiUiCustomBuilderDialogFragment @JvmOverloads constructor(builder: Builder=Builder() ):
    BaseDialogFragment<DeiUiCustomBuilderDialogFragment.Builder>(builder) {


    override fun initView() {

    }


    class Builder : BaseDialogFragment.Builder<Builder>() {

        fun build(): DeiUiCustomBuilderDialogFragment {
            dialogFragment = DeiUiCustomBuilderDialogFragment(this)
            return dialogFragment as DeiUiCustomBuilderDialogFragment
        }
    }

}
