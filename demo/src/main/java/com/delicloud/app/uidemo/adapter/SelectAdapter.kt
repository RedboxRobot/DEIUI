package com.delicloud.app.uidemo.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.delicloud.app.uidemo.R

/**
 * 选择框Adapter，分为多选和单选，带图不带图
 *Created By Mr.m
 *2019/7/24
 **/
class SelectAdapter(private val context:Context,val data: List<String>, val showImg: Boolean, val multiChoice: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //存储选择的位置
    val choiceSet = mutableSetOf<Int>()
    //单选中上次选择的View
    var oldCheckedView: View? = null
    var leftDrawable: Drawable?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(
           //根据单选还是多选加载各自布局
            LayoutInflater.from(parent.context).inflate(
                if (multiChoice) R.layout.deiui_item_checkbox else R.layout.deiui_item_radiobutton,
                parent,
                false
            )
        ) {}
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //显示或隐藏图片
        holder.itemView.findViewById<TextView>(R.id.item_radiobutton_tv)?.apply {
            setCompoundDrawablesWithIntrinsicBounds(if (showImg)leftDrawable else null, null, null, null)
        }
        holder.itemView.findViewById<TextView>(R.id.item_checkbox_tv)?.apply {
            setCompoundDrawablesWithIntrinsicBounds(if (showImg) leftDrawable else null, null, null, null)
        }
        holder.itemView.setOnClickListener {
            //已选择则取消选择
            if (choiceSet.contains(position))
                choiceSet.remove(position)
            else
                choiceSet.add(position)
            if (multiChoice)
                holder.itemView.findViewById<CheckBox>(R.id.item_checkbox)?.let {
                    it.isChecked = !it.isChecked
                }
            else {
                if (oldCheckedView != holder.itemView) {
                   //取消单选中上一个选择的View
                    oldCheckedView?.findViewById<RadioButton>(R.id.item_radiobutton)?.isChecked = false
                }
                holder.itemView.findViewById<RadioButton>(R.id.item_radiobutton)?.let {
                    it.isChecked = !it.isChecked
                    if (it.isChecked) {
                        //把本次选择的itemView赋值给oldCheckedView
                        oldCheckedView = holder.itemView
                    }
                }
            }

        }
    }

}