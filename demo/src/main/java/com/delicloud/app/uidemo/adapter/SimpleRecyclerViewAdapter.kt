package com.delicloud.app.uidemo.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.delicloud.app.uidemo.R

/**
 * 数据录入列表项Adapter
 * data 数据
 * itemtype 列表类型
 *Created By Mr.m
 *2019/7/23
 **/
class SimpleRecyclerViewAdapter(private val data: List<String>,  val itemType: ItemType) :

    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = when (itemType) {
            ItemType.SINGLE_TITLE_WITH_IMG, ItemType.SINGLE_TITLE_WITHOUT_IMG -> {
                //加载单标题布局
                LayoutInflater.from(parent.context).inflate(R.layout.deiui_item_single_title, parent, false)
            }
            ItemType.SINGLE_DESC_WITH_IMG, ItemType.SINGLE_DESC_WITHOUT_IMG -> {
                //加载单描述布局
                LayoutInflater.from(parent.context).inflate(R.layout.deiui_item_single_desc, parent, false)
            }
            ItemType.MULTI_DESC_WITHOUT_IMG -> {
                //加载多描述不带图布局
                LayoutInflater.from(parent.context).inflate(R.layout.deiui_item_multi_desc_without_img, parent, false)
            }
            ItemType.MULTI_DESC_WITH_IMG -> {
                //加载多描述带图布局
                LayoutInflater.from(parent.context).inflate(R.layout.deiui_item_multi_desc_with_img, parent, false)
            }
        }
        return object : RecyclerView.ViewHolder(view) {

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (itemType) {
            ItemType.MULTI_DESC_WITH_IMG, ItemType.SINGLE_DESC_WITH_IMG, ItemType.SINGLE_TITLE_WITH_IMG -> {
                //带图时设置图片可见
                holder.itemView.findViewById<ImageView>(R.id.item_iv).apply {
                    visibility = View.VISIBLE
                    background = ColorDrawable(Color.parseColor("#cccccc"))
                }
            }
        }
    }

    /**
     * 列表样式
     * 单标题不带图，单标题带图，单描述不带图，单描述带图，多描述不带图，多描述带图
     */
    enum class ItemType {
        SINGLE_TITLE_WITHOUT_IMG, SINGLE_TITLE_WITH_IMG, SINGLE_DESC_WITHOUT_IMG, SINGLE_DESC_WITH_IMG, MULTI_DESC_WITHOUT_IMG, MULTI_DESC_WITH_IMG
    }
}