package com.delicloud.app.uidemo.fragment


import android.text.InputType
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.delicloud.app.deiui.feedback.dialog.*
import com.delicloud.app.uidemo.R
import com.delicloud.app.uidemo.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_feedback_dialog.*

/**
 * 弹窗界面
 */
class FeedbackDialogFragment : BaseFragment() {


    override fun layoutId(): Int {
        return R.layout.fragment_feedback_dialog
    }

    override fun initView() {
        single_btn_dialog_btn.setOnClickListener { showOneButton() }
        double_btn_dialog_btn.setOnClickListener { showDoubleButton() }
        multi_btn_dialog_btn.setOnClickListener { showMultiBtnDialog(true) }
        multi_btn_list_dialog_btn.setOnClickListener { showMultiBtnDialog(false) }
        without_title_dialog_btn.setOnClickListener { showDoubleButtonWithoutTitle() }
        with_img_dialog_btn.setOnClickListener { showDialogWithImg() }
        with_big_img_dialog_btn.setOnClickListener { showDoubleButtonWithBigImage() }
        double_btn_quote_dialog_btn.setOnClickListener { showDoubleButtonQuoteDialog() }
        activity_dialog.setOnClickListener { showActivityDialog() }
        progress_horizontal_dialog_btn.setOnClickListener {
            showHorizontalProgressDialog()
        }
        edit_dialog_btn.setOnClickListener { showInputDialog() }
        apply_permission_dialog_btn.setOnClickListener { showApplyPermissionDialog() }
        bottom_dialog_btn.setOnClickListener {
            showBottomDialog()
        }
        custom_dialog_btn.setOnClickListener {
            showCustomDialog()
        }
    }

    override fun initData() {
    }

    /**
     * 单按钮弹窗
     */
    fun showOneButton() {
        DeiUiDialogFragmentHelper.createOneButtonDialog(context!!,
            "单行标题",
            "描述文字的字数最好控制在三行描述文字的字数最好控制在三行描述文字的字数最好控制在三行描述文字的字数最好控制在三行描述文字的字数最好控制在三行",
            "确定",
            true,
            View.OnClickListener { }
        ).show(fragmentManager, "单按钮")
    }

    /**
     * 双按钮弹窗
     */
    fun showDoubleButton() {
        val dialog = DeiUiDialogFragmentHelper.createDoubleButtonWithTitleDialog(
            context!!,
            getString(R.string.DialogTitle),
            getString(R.string.DialogMessage),
            "主操作",
            "辅助操作",
            true,
            object : DeiUiDialogFragmentHelper.OnDialogActionListener {
                override fun doCancelAction() {
                }

                override fun doOkAction() {
                }
            }
        )
        dialog.show(fragmentManager, "两个按钮")
    }

    /*
    *多操作按钮弹窗
    * withTitleMessage 是否带标题和描述信息
    * */
    fun showMultiBtnDialog(withTitleMessage: Boolean) {
        val dialog = DeiUiListItemDialogFragment.Builder()
            .apply {
                if (withTitleMessage) {
                    setTitle(this@FeedbackDialogFragment.getString(R.string.DialogTitle))
                    setMessage(this@FeedbackDialogFragment.getString(R.string.DialogMessage))
                }
            }.addItem("选项一", object : DeiUiListItemDialogFragment.onSeparateItemClickListener {
                override fun onClick() {

                }
            }).addItem("选项一", object : DeiUiListItemDialogFragment.onSeparateItemClickListener {
                override fun onClick() {

                }
            }).addItem("选项一", object : DeiUiListItemDialogFragment.onSeparateItemClickListener {
                override fun onClick() {

                }
            }).build()
        dialog.show(fragmentManager, "列表")
    }

    /**
     * 带图弹窗
     */
    fun showDialogWithImg() {
        val dialog = DeiUiDialogFragmentHelper.createOneButtonWithImgDialog(
            context!!,
            getString(R.string.DialogTitle),
            getString(R.string.DialogMessage),
            getString(R.string.DialogPositiveText),
            true,
            View.OnClickListener { }
        )
        //设置图片资源
        dialog.mBuilder.setDrawableRes(R.drawable.ic_dialog_big_temp)
        dialog.show(fragmentManager, "")
    }

    /**
     * 双操作按钮不带标题
     */
    fun showDoubleButtonWithoutTitle() {
        val dialog = DeiUiDialogFragmentHelper.createDoubleButtonWithoutTittleDialog(
            context!!,
            getString(R.string.DialogMessage),
            getString(R.string.DialogPositiveText),
            getString(R.string.DialogNegativeText),
            true,
            object : DeiUiDialogFragmentHelper.OnDialogActionListener {
                override fun doCancelAction() {

                }

                override fun doOkAction() {
                }
            }
        )
        dialog.show(fragmentManager, "双按钮不带标题")
    }

    /**
     * 双按钮带大图
     */
    fun showDoubleButtonWithBigImage() {
        val dialog = DeiUiDialogFragmentHelper.createDoubleButtonWithBigImgDialog(
            context!!,
            getString(R.string.DialogTitle),
            getString(R.string.DialogMessage),
            "主操作",
            "辅助操作",
            true,
            null
        )
        dialog.mBuilder.setDrawableRes(R.drawable.dialog_img)
        dialog.show(fragmentManager, "两个按钮")
    }

    /**
     * 双按钮引用弹窗
     */
    fun showDoubleButtonQuoteDialog() {
        val dialog = DeiUiDialogFragmentHelper.createDoubleButtonQuoteDialog(
            context!!,
            getString(R.string.DialogTitle),
            getString(R.string.DialogMessage),
            "主操作",
            "辅助操作",
            true,
            null
        )
        //设置引用弹窗图
        dialog.mBuilder.setDrawableRes(R.drawable.ic_dialog_medium_temp)
        dialog.show(fragmentManager, "引用弹窗")
    }

    /**
     * 申请授权弹窗
     */
    fun showApplyPermissionDialog() {
        val list = arrayListOf<String>()
        list.apply {
            for (i in 0 until 3)
                add("描述文字的字数最好控制在两行，右侧不能有标点符号")
        }
        DeiUiUpgradeDialogFragment.Builder()
            .setUpgradeList(list)
            .isCancelable(true)
            .setDialogActionListener(
                object : DeiUiUpgradeDialogFragment.OnDialogActionListener {
                    override fun onButtonClick() {

                    }

                    override fun onCancel() {
                    }
                })
            .setBtnStr(getString(R.string.DialogPositiveText))
            .setTitle(getString(R.string.DialogTitle))
            .setContentTitle("描述信息标题")
            .setLogoDrawableRes(R.drawable.ic_dialog_medium_temp)
            .build()
            .show(fragmentManager, "申请授权弹窗")

    }

    /**
     * 活动运营弹窗
     */
    fun showActivityDialog() {
        DeiUiDialogFragmentHelper.createActivityOperationDialog(
            context!!,
            "单行标题",
            "描述文字的字数最好控制在两行",
            "点击进入",
            true,
            View.OnClickListener { })

        val dialogFragment = DeiUiDialogFragment.Builder()
            .view(R.layout.deiui_alert_dialog_activity)
            .setImageUrl("https://file.delicloud.com/banner/mantoutanchuang.png")
            .addPositiveBtn("点击进入", object : BaseDialogFragment.OnClickListener {
                override fun onClick(dialogFragment: DialogFragment, view: View) {
                    Toast.makeText(context, "点击入口", Toast.LENGTH_SHORT).show()
                }
            }).setTitle("单行标题")
            .setMessage("描述文字的字数最好控制在两行")
            .build()
        dialogFragment.show(fragmentManager, "活动运营弹窗")
    }

    /**
     * 水平进度条弹窗
     */
    fun showHorizontalProgressDialog() {
        val dialog = DeiUiDialogFragmentHelper.createDoubleButtonHorizontalProgressDialog(
            context!!,
            getString(R.string.DialogTitle),
            getString(R.string.DialogMessage),
            "主操作",
            "辅助操作",
            100,
            null
        )
        dialog.show(fragmentManager, "两个按钮")
        var p = 10
        dialog.setProgress(p)
    }

    /**
     * 输入弹窗
     */
    fun showInputDialog() {
        DeiUiEditDialogFragment.Builder()
            .setEditHint("请编辑信息")
            .setEditTextMaxLength(16)
            .setTitle(getString(R.string.DialogTitle))
            .setMessage("描述文字的字数最好控制在两行")
            .setInputType(InputType.TYPE_CLASS_NUMBER)
            .addNegativeBtn()
            .addEditPositiveBtn(listener = object :
                DeiUiEditDialogFragment.OnPositiveClickListener {
                override fun onClick(dialogFragment: DeiUiEditDialogFragment, msg: String?) {
                    dialogFragment.dismiss()
                    Toast.makeText(activity, msg ?: "empty", Toast.LENGTH_SHORT).show()
                }
            }).build()
            .show(fragmentManager, "输入弹窗")
    }

    /**
     * 底部弹窗
     */
    fun showBottomDialog() {
        DeiUiActionSheetDialogFragment.Builder()
            .isCancelable(true)
            .setCancelTextColor(DeiUiActionSheetDialogFragment.SheetItemColor.Blue)
            .cancelTouchOut(true)
            .addSheetItem(
                "选项一", DeiUiActionSheetDialogFragment.SheetItemColor.Blue
                , object : DeiUiActionSheetDialogFragment.OnSheetItemClickListener {
                    override fun onClick(which: Int) {
                    }
                })
            .addSheetItem(
                "选项2",
                DeiUiActionSheetDialogFragment.SheetItemColor.Blue,
                object : DeiUiActionSheetDialogFragment.OnSheetItemClickListener {
                    override fun onClick(which: Int) {

                    }
                }).addSheetItem(
                "选项三",
                DeiUiActionSheetDialogFragment.SheetItemColor.Red,
                object : DeiUiActionSheetDialogFragment.OnSheetItemClickListener {
                    override fun onClick(which: Int) {

                    }
                }).addSheetItem(
                "选项四",
                DeiUiActionSheetDialogFragment.SheetItemColor.Gray,
                object : DeiUiActionSheetDialogFragment.OnSheetItemClickListener {
                    override fun onClick(which: Int) {

                    }
                })
            .cancelTouchOut(true)
            .build()
            .show(fragmentManager, "")
    }

    /**
     * 自定义弹窗
     */
    fun showCustomDialog() {
        val deiUiCustomBuilderDialogFragment = DeiUiCustomBuilderDialogFragment.Builder()
            .view(R.layout.deiui_dialog_edit_name)
            .cancelTouchOut(false)
            .build()
        //设置显示监听,有关View操作需在show后进行
        deiUiCustomBuilderDialogFragment.run {
            addChildListener(R.id.dialog_positive, View.OnClickListener {
                Toast.makeText(
                    activity,
                    getChildView<TextView>(R.id.dialog_edit)?.text ?: "输入为空",
                    Toast.LENGTH_SHORT
                ).show()
                deiUiCustomBuilderDialogFragment.dismiss()
            })
            addChildListener(R.id.dialog_negative, View.OnClickListener {
                deiUiCustomBuilderDialogFragment.dismiss()
            })
        }

        deiUiCustomBuilderDialogFragment.show(fragmentManager)
    }
}
