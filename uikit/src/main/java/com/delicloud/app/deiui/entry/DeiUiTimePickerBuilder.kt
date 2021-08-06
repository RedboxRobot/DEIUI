package com.delicloud.app.deiui.entry

import android.content.Context
import android.widget.Button
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.delicloud.app.deiui.R

/**
 * 时间选择器
 *Created By Mr.m
 *2019/7/25
 **/
class DeiUiTimePickerBuilder(val context: Context, val listener: OnTimeSelectListener) :
    TimePickerBuilder(context, listener) {
    var pickerView: TimePickerView? = null
    //获取常用时间选择器
    fun getCustomPickerView(showType:BooleanArray): TimePickerView {
        setType(showType)//设置年月日时分秒显示状态默认全部显示
        //设置常用自定义选择器视图
        setLayoutRes(R.layout.deiui_pickerview) { v ->
            v?.findViewById<Button>(R.id.btnSubmit)?.setOnClickListener {
                //返回数据
                pickerView?.returnData()
                pickerView!!.dismiss()
            }
            v?.findViewById<Button>(R.id.btnCancel)?.setOnClickListener {
                pickerView?.dismiss()
            }
        }
        setContentTextSize(16)//内容文字大小
        setTextColorCenter(ContextCompat.getColor(context, R.color.colorAccent))//被选择文字颜色
        setDividerColor(ContextCompat.getColor(context, R.color.deiui_time_picker_divider_color))
        setLineSpacingMultiplier(2.5f)//通过设置间距陪数，控制每个item高度
        pickerView = build()
        return pickerView!!

    }

}