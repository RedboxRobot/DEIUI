package com.delicloud.app.uidemo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.delicloud.app.deiui.entry.*
import com.delicloud.app.deiui.entry.DeiUiShareDialogFragment.ShareItem
import com.delicloud.app.deiui.utils.ScreenUtil
import com.delicloud.app.uidemo.R
import com.delicloud.app.uidemo.adapter.SelectAdapter
import com.delicloud.app.uidemo.adapter.SimpleRecyclerViewAdapter
import com.delicloud.app.uidemo.extentions.contentView
import com.delicloud.app.uidemo.utils.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_entry_detail.*
import kotlinx.android.synthetic.main.entry_list_layout.*
import kotlinx.android.synthetic.main.entry_operation_board_layout.*
import kotlinx.android.synthetic.main.entry_select_layout.*
import kotlinx.android.synthetic.main.operate_step_layout.*
import kotlinx.android.synthetic.main.share_layout.*
import org.jetbrains.anko.topPadding
import java.util.*


@Suppress("UNREACHABLE_CODE")
class EntryDetailActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var searchTv: TextView
    private lateinit var activeSearchView: DeiUiSearchView
    private lateinit var activeSearchViewLinearLayout: LinearLayout
    private val RC_CHOOSE_PHOTO = 0x0c
    private val imgList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_detail)
        setDarkStatusIcon(true)
        if (savedInstanceState != null) {
            code = savedInstanceState.getInt("code")
            imgList.addAll(savedInstanceState.getStringArrayList("imgList"))
        }
        initView()
    }

    private fun initView() {
        initCommonView()

        when (code) {
            REQUEST_CODE_SEARCH_NORMAL -> {
                initSearchViewNormal()
            }
            REQUEST_CODE_SEARCH_ACTIVE -> {
                searchView_active_ViewStub.visibility = View.VISIBLE
                activeSearchView = findViewById(R.id.active_searchview)
                activeSearchViewLinearLayout = findViewById(R.id.active_search_ll)
                contentView?.topPadding = getStatusBarHeight()
                setStatusTransAndDarkIcon(
                    ContextCompat.getColor(
                        this,
                        R.color.deiui_window_background
                    )
                )
                toolbar.visibility = View.GONE
                activeSearchViewLinearLayout.visibility = View.VISIBLE
            }
            REQUEST_CODE_BUTTON -> {
                button_viewStub.visibility = View.VISIBLE
                findViewById<Button>(R.id.small_operate_btn).setOnClickListener { view ->
                    view.isEnabled = false
                }
                findViewById<Button>(R.id.main_operate_btn).setOnClickListener { view ->
                    view.isEnabled = false
                }
                findViewById<Button>(R.id.second_operate_btn).setOnClickListener { view ->
                    view.isEnabled = false
                }
                findViewById<Button>(R.id.third_operate_btn).setOnClickListener { view ->
                    view.isEnabled = false
                }
            }
            REQUEST_CODE_LIST -> {
                initListView()
            }
            REQUEST_CODE_SELECT -> {
                initSelectView()

            }
            REQUEST_CODE_OPERATE -> {
                initOperateView()
            }
            REQUEST_CODE_SHARE -> {
                initShareView()
            }
            REQUEST_CODE_FORM -> {
                form_viewStub.visibility = View.VISIBLE
            }
        }

        setListener()
    }

    /**
     * 分享
     */
    fun initShareView() {
        share_viewStub.visibility = View.VISIBLE
        //分享应用列表
        val shareItems =
            listOf(ShareItem(R.mipmap.deiui_ic_qq, "QQ"), ShareItem(R.mipmap.deiui_ic_wechat, "微信"))
        share_two_item_btn.setOnClickListener {
            //两个应用分享弹窗
            DeiUiShareDialogFragment.Builder()
                .setTitle("立即分享")
                .setColumnCount(2)
                .setItemList(shareItems)
                .setOnShareItemClickListener(object :
                    DeiUiShareDialogFragment.OnShareItemClickListener {
                    override fun onClick(position: Int, appName: String) {

                    }
                }).build()
                .show(supportFragmentManager)

        }
        share_three_item_btn.setOnClickListener {
            //三个应用分享弹窗
            DeiUiShareDialogFragment.Builder()
                .setTitle("立即分享")
                .setColumnCount(3)
                .setItemList(shareItems)
                .addItemView(shareItems.first())
                .cancelTouchOut(true)
                .build()
                .show(this@EntryDetailActivity.supportFragmentManager, "分享")
        }
        share_six_item_btn.setOnClickListener {
            //六个应用分享弹窗
            DeiUiShareDialogFragment.Builder()
                .setTitle("立即分享")
                .setItemList(shareItems)
                .apply {
                    shareItems.forEach {
                        addItemView(it)
                        addItemView(it)
                    }

                }.build()
                .show(this@EntryDetailActivity.supportFragmentManager, "分享")

        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.i(localClassName, "onSaveInstanceState")
        outState?.putInt(
            "code",
            code
        )
        outState?.putStringArrayList("imgList", imgList)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(localClassName, "onRestoreInstanceState")
    }


    /**
     * 操作面板
     */
    private fun initOperateView() {
        operate_viewStub.visibility = View.VISIBLE
        initDatePicker()
        initImageSelector()
        //设置不可用
        enable_sb.isEnabled = false
        //设置选择
        enable_on_sb.check = true
        enable_on_sb.isEnabled = false
        stepView.inputIllegalListener = object : DeiUiStepView.InputStepIllegalListener {
            override fun onStepIllegal(step: Float) {
                if (step < stepView.min) {
                    Log.i("StepView", "值太小")
                    stepView.setStep(stepView.min)
                }
                if (step > stepView.max) {
                    Log.i("StepView", "值太大")
                    stepView.setStep(stepView.max)
                }
            }
        }
    }

    /**
     * 图片选择器
     */
    private fun initImageSelector() {
        num_tv.text = "0/8"
        image_selector_recyclerview.let {
            it.layoutManager = StaggeredGridLayoutManager(4, RecyclerView.VERTICAL)
            it.adapter = DeiUiPhotoPickerAdapter(this@EntryDetailActivity, imgList).apply {
                DeiUiPhotoPickerAdapter.MAX = 8
                onItemClickListener = object : DeiUiPhotoPickerAdapter.OnItemClickListener {
                    override fun onItemChildClick(v: View, p: Int) {
                        when (v.id) {
                            R.id.item_delete_iv -> {
                                //删除图片
                                imgList.removeAt(p)
                                notifyDataSetChanged()
                                num_tv.text = "${imgList.size}/${DeiUiPhotoPickerAdapter.MAX}"
                            }
                            R.id.item_selector_iv -> {
                                //查看图片
                            }
                            R.id.add_photo -> {
                                //请求权限
                                requestPermission()
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * 请求读取内存权限
     */
    fun requestPermission() {
        //检查权限
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                RC_CHOOSE_PHOTO
            )
        } else {
            //已授权，获取照片
            choosePhoto()
        }
    }

    /**
     * 选择照片
     */
    fun choosePhoto() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //获取返回的图片
        if (requestCode == RC_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.i(localClassName, "onActivityResult Start")
            val uri = data?.data
            Log.i(localClassName, uri.toString())
            val filePath = SystemFileUtils.uriToPath(this, uri)
            Log.i(localClassName, "filePath$filePath")
            imgList.add(filePath)
            image_selector_recyclerview.adapter?.notifyDataSetChanged()
            num_tv.text = "${imgList.size}/${DeiUiPhotoPickerAdapter.MAX}"
            Log.i(localClassName, "onActivityResult finished")
        }
    }

    override fun onDestroy() {
        Log.i(localClassName, "onDestroy")
        super.onDestroy()
    }

    /**
     * 日期选择
     */
    @SuppressLint("SetTextI18n")
    private fun initDatePicker() {
        findViewById<TextView>(R.id.item_title_tv).text = "选择日期"
        val descTv = findViewById<TextView>(R.id.item_desc_tv)
        descTv.text = ""
        descTv.hint = "2000-10-10"
        findViewById<LinearLayout>(R.id.item_single_title_root).apply {
            background = ColorDrawable(Color.WHITE)
            setOnClickListener {
                DeiUiTimePickerBuilder(this@EntryDetailActivity, OnTimeSelectListener { date, v ->
                    //得到选择的日期
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    descTv.text =
                        "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}-${calendar.get(
                            Calendar.DAY_OF_MONTH
                        )}"
                })
                    //获取通用日期选择器，显示类型，年月日时分秒都显示,注意booleanArray长度为6否则会报错
                    .getCustomPickerView(booleanArrayOf(true, true, true, true, true, true))
                    .show()
            }
        }
    }

    /**
     * 列表
     */
    private fun initListView() {
        list_viewStub.visibility = View.VISIBLE
        val listNames = resources.getStringArray(R.array.list_names)
        for (item in listNames) {
            list_tab_layout.addTab(list_tab_layout.newTab().setText(item))
        }
        val recyclerItems = arrayListOf("多行列表", "多行列表", "多行列表")
        val adapterArrays = arrayOf(
            SimpleRecyclerViewAdapter.ItemType.SINGLE_TITLE_WITHOUT_IMG,
            SimpleRecyclerViewAdapter.ItemType.SINGLE_DESC_WITHOUT_IMG,
            SimpleRecyclerViewAdapter.ItemType.MULTI_DESC_WITHOUT_IMG,
            SimpleRecyclerViewAdapter.ItemType.SINGLE_TITLE_WITH_IMG,
            SimpleRecyclerViewAdapter.ItemType.SINGLE_DESC_WITH_IMG,
            SimpleRecyclerViewAdapter.ItemType.MULTI_DESC_WITH_IMG
        )
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter =
            SimpleRecyclerViewAdapter(
                recyclerItems,
                SimpleRecyclerViewAdapter.ItemType.SINGLE_TITLE_WITHOUT_IMG
            )
        addDecoration()
        list_tab_layout.addOnTabSelectedListener(object :
            TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                //切换Adapter
                val adapter =
                    SimpleRecyclerViewAdapter(recyclerItems, adapterArrays[p0?.position ?: 0])
                recyclerView.adapter = adapter
                if (recyclerView.itemDecorationCount > 0)
                    recyclerView.removeItemDecorationAt(0)
                if (adapter.itemType != SimpleRecyclerViewAdapter.ItemType.MULTI_DESC_WITHOUT_IMG) {
                    addDecoration()
                    recyclerView.setBackgroundColor(Color.WHITE)
                } else {
                    recyclerView.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        })

    }

    //设置分割线
    fun addDecoration() {
        val itemDecoration = ColorDividerItemDecoration(
            ContextCompat.getColor(
                this,
                R.color.deiui_items_divider_color
            )
        )
        val itemType = (recyclerView.adapter as SimpleRecyclerViewAdapter).itemType
        if (itemType in arrayOf(
                SimpleRecyclerViewAdapter.ItemType.SINGLE_TITLE_WITH_IMG,
                SimpleRecyclerViewAdapter.ItemType.SINGLE_DESC_WITH_IMG,
                SimpleRecyclerViewAdapter.ItemType.MULTI_DESC_WITH_IMG
            )
        ) {
            itemDecoration.mDividerMarginLeft = ScreenUtil.dip2px(15f)
        }
        recyclerView.addItemDecoration(itemDecoration)
    }

    /**
     * 选择框
     */
    private fun initSelectView() {
        select_viewStub.visibility = View.VISIBLE
        val itemDatas = arrayListOf<String>("选项一", "选项二", "选项三")
        val itemDecoration = ColorDividerItemDecoration(
            ContextCompat.getColor(
                this@EntryDetailActivity,
                R.color.deiui_items_divider_color
            )
        )
        itemDecoration.mDividerMarginLeft = ScreenUtil.dip2px(15f)
        val selectedDrawable = ContextCompat.getDrawable(
            this,
            R.drawable.ic_select
        )
        img_rb_rv.apply {
            adapter = SelectAdapter(
                this@EntryDetailActivity,
                itemDatas,
                showImg = true,
                multiChoice = false
            ).apply {
                leftDrawable = selectedDrawable
            }
            layoutManager =
                LinearLayoutManager(this@EntryDetailActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(itemDecoration)
        }

        rb_rv.apply {
            adapter = SelectAdapter(
                this@EntryDetailActivity,
                itemDatas,
                showImg = false,
                multiChoice = false
            )
            layoutManager =
                LinearLayoutManager(this@EntryDetailActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(itemDecoration)
        }
        img_cb_rv.apply {
            adapter = SelectAdapter(
                this@EntryDetailActivity,
                itemDatas,
                showImg = true,
                multiChoice = true
            ).apply {
                leftDrawable = selectedDrawable
            }
            layoutManager =
                LinearLayoutManager(this@EntryDetailActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(itemDecoration)
        }
        cb_rv.apply {
            adapter = SelectAdapter(
                this@EntryDetailActivity,
                itemDatas,
                showImg = false,
                multiChoice = true
            )
            layoutManager =
                LinearLayoutManager(this@EntryDetailActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(itemDecoration)
        }

    }

    /**
     * 初始化共同的View
     */
    private fun initCommonView() {
        toolbar = findViewById(R.id.single_title_toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        findViewById<TextView>(R.id.toolbar_title_tv).text = "数据录入"

    }

    fun initSearchViewNormal() {
        searchView_normal_viewStub.visibility = View.VISIBLE
        searchTv = findViewById(R.id.search_tv)
        searchTv.setOnClickListener {
            start(
                this,
                REQUEST_CODE_SEARCH_ACTIVE
            )
        }
    }

    /**
     * 设置公共监听
     */
    private fun setListener() {
        if (this::activeSearchView.isInitialized) {
            findViewById<TextView>(R.id.cancel_search_tv)?.setOnClickListener { finish() }
            activeSearchView.setOnEditorActionListener { textView, i, keyEvent ->
                when (i) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        //点击搜索后清空文本
                        activeSearchView.text?.clear()
                        return@setOnEditorActionListener true
                    }
                    else -> {
                        return@setOnEditorActionListener false
                    }
                }
            }
            activeSearchView.setOnDropArrowClickListener(object :
                DeiUiSearchView.OnDropArrowClickListener {
                override fun onDropArrowClick() {
                    //点击删除清空文本
                    activeSearchView.text?.clear()
                }
            })
        }

    }

    companion object {
        const val REQUEST_CODE_SEARCH_NORMAL = 0x01
        const val REQUEST_CODE_SEARCH_ACTIVE = 0x02
        const val REQUEST_CODE_BUTTON = 0x03
        const val REQUEST_CODE_LIST = 0x04
        const val REQUEST_CODE_SELECT = 0x05
        const val REQUEST_CODE_OPERATE = 0x06
        const val REQUEST_CODE_SHARE = 0x07
        const val REQUEST_CODE_FORM = 0x08
        private var code: Int = 0x01
        fun start(context: Context, code: Int) {
            Companion.code = code
            context.startActivity(Intent(context, EntryDetailActivity::class.java))
        }
    }
}
