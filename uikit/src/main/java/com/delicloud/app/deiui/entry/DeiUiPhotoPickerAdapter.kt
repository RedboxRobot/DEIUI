package com.delicloud.app.deiui.entry

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.utils.AndroidLifecycleUtils
import java.io.File
import java.util.*

/**
 * 图片选择器 Adapter
 * @author ChengXinPing
 * @time 2017/12/5 17:31
 */
class DeiUiPhotoPickerAdapter(private val mContext: Context, photoPaths: ArrayList<String>) :
    RecyclerView.Adapter<DeiUiPhotoPickerAdapter.PhotoViewHolder>() {

    private var photoPaths = ArrayList<String>()
    private val inflater: LayoutInflater
    var onItemClickListener: OnItemClickListener? = null

    init {
        this.photoPaths = photoPaths
        inflater = LayoutInflater.from(mContext)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView: View = when (viewType) {
            //返回添加图片view
            TYPE_ADD -> inflater.inflate(R.layout.deiui_item_image_selector_footview, parent, false)
            else ->
                //返回图片item
                inflater.inflate(R.layout.deiui_item_image_selector, parent, false)
        }
        return PhotoViewHolder(itemView)
    }


    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_PHOTO) {
            val uri = Uri.fromFile(File(photoPaths[position]))
            //判断是否可以加载图片
            val canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto?.context)
            if (canLoadImage) {
                val options = RequestOptions()
                    .placeholder(R.drawable.deiui_picker_ic_photo_black_48dp)
                    .error(R.drawable.deiui_picker_ic_broken_image_black_48dp)
                Glide.with(mContext)
                    .load(uri)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .apply(options)
                    .into(holder.ivPhoto!!)
            }
            holder.ivPhoto?.setOnClickListener {
                onItemClickListener?.onItemChildClick(it, position)
            }
            holder.ivDelete?.setOnClickListener {
                onItemClickListener?.onItemChildClick(it, position)
            }
        } else {
            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemChildClick(holder.itemView, position)
            }
        }
    }


    override fun getItemCount(): Int {
        //count包含图片列表数和添加图片item
        var count = photoPaths.size + 1
        if (count > MAX) {
            count = MAX
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == photoPaths.size && position != MAX) TYPE_ADD else TYPE_PHOTO
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivPhoto: ImageView? = null
        var ivDelete: ImageView? = null

        init {
            ivPhoto = itemView.findViewById<View>(R.id.item_selector_iv) as? ImageView
            ivDelete = itemView.findViewById(R.id.item_delete_iv)
        }
    }

    /**
     * 点击监听
     */
    interface OnItemClickListener {
        fun onItemChildClick(v: View, p: Int)
    }

    companion object {
        //Item类型-添加
        val TYPE_ADD = 1
        //Item类型-图片
        val TYPE_PHOTO = 2
        //最大选择数
        var MAX = 16
    }

}