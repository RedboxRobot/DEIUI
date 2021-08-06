package com.delicloud.app.deiui.entry

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.delicloud.app.deiui.R

/**
 * 开关按钮
 *
 * @author sunyoujun
 */
class DeiUiSwitchButton : View, View.OnTouchListener {
    /**
     * 记录当前按钮是否打开,true为打开, false为关闭
     */
    /**
     * 判断开关是否打开
     *
     * @return 开关的状态
     */
    var isChoose = false
        private set

    private var isChecked: Boolean = false
    /**
     * 记录用户是否在滑动的变量
     */
    private var onSlip = false
    /**
     * 按下时的x,当前的x
     */
    private var downX: Float = 0.toFloat()
    private var nowX: Float = 0.toFloat()
    /**
     * 打开和关闭状态下,游标的Rect .
     */
    private lateinit var btnOffRect: Rect
    private lateinit var btnOnRect: Rect

    private var isChangeOn = false

    private var isInterceptOn = false

    private var onChangedListener: OnChangedListener? = null

    private lateinit var bgOnBmp: Bitmap
    private lateinit var bgOffBmp: Bitmap
    private lateinit var slipBtnBmp: Bitmap
    /**
     * 图片缩放比例
     */
    var sx: Float = 0f
    var sy: Float = 0f

    private val bgMatrix = Matrix()
    private val slipMatrix = Matrix()

    var check: Boolean
        get() = this.isChecked
        set(isChecked) {
            this.isChecked = isChecked
            isChoose = isChecked
            if (!isChecked) {
                nowX = 0f
            }
            invalidate()
        }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs)
    }


    private fun init(attrs: AttributeSet?) {// 初始
        val attr = context.obtainStyledAttributes(
            attrs,
            R.styleable.DeiUiSwitchButton
        )
        val bgOnBmpAttr =
            (attr.getDrawable(R.styleable.DeiUiSwitchButton_switch_on_bg) as? BitmapDrawable)?.bitmap
        val bgOffBmpAttr =
            ((attr.getDrawable(R.styleable.DeiUiSwitchButton_switch_off_bg)) as? BitmapDrawable)?.bitmap
        val slipBmpAttr =
            ((attr.getDrawable(R.styleable.DeiUiSwitchButton_slip_src)) as? BitmapDrawable)?.bitmap
        bgOnBmp = bgOnBmpAttr ?: BitmapFactory.decodeResource(
            resources,
            R.drawable.deiui_custom_sw_slide_toggle_on
        )
        bgOffBmp = bgOffBmpAttr ?: BitmapFactory.decodeResource(
            resources,
            R.drawable.deiui_custom_sw_slide_toggle_off
        )
        slipBtnBmp = slipBmpAttr ?: BitmapFactory.decodeResource(
            resources,
            R.drawable.deiui_custom_sw_slide_toggle
        )

        //设置监听器,也可以直接复写OnTouchEvent
        setOnTouchListener(this)
        attr.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        sx = measuredWidth.toFloat() / bgOnBmp.width
        sy = measuredHeight.toFloat() / bgOnBmp.height
        bgMatrix.setScale(sx, sy)
        btnOffRect = Rect(0, 0, (slipBtnBmp.width*sx).toInt(), (slipBtnBmp.height*sy).toInt())
        btnOnRect =
            Rect(((bgOffBmp.width - slipBtnBmp.width)*sx).toInt(), 0, (bgOffBmp.width*sx).toInt(), (slipBtnBmp.height*sy).toInt())
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {// 绘图函数
        super.onDraw(canvas)

        val paint = Paint()
        var x: Float
        // 滑动到前半段与后半段的背景不同,在此做判断
        if (nowX < width / 2) {
            canvas.drawBitmap(bgOffBmp, bgMatrix, paint)// 画出关闭时的背景
        } else {
            canvas.drawBitmap(bgOnBmp, bgMatrix, paint)// 画出打开时的背景
        }
        // 是否是在滑动状态
        if (onSlip) {
            x = when {
                nowX >= width -> // 是否划出指定范围,不能让游标跑到外头,必须做这个判断
                    width - slipBtnBmp.width * sx / 2// 减去游标1/2的长度...
                nowX < 0 -> 0f
                else -> nowX - slipBtnBmp.width * sx / 2
            }
        } else {// 非滑动状态
            if (isChoose) {// 根据现在的开关状态设置画游标的位置
                x = btnOnRect.left.toFloat()
                canvas.drawBitmap(bgOnBmp, bgMatrix, paint)// 初始状态为true时应该画出打开状态图片
            } else {
                x = btnOffRect.left.toFloat()
            }
        }
        if (isChecked) {
            canvas.drawBitmap(bgOnBmp, bgMatrix, paint)
            x = btnOnRect.left.toFloat()
            isChecked = !isChecked
        }

        // 对游标位置进行异常判断...
        if (x < 0) {
            x = 0f
        } else if (x > width - slipBtnBmp.width * sx) {
            x = width - slipBtnBmp.width * sx
        }
        slipMatrix.reset()
        slipMatrix.postScale(sx, sy)
        slipMatrix.postTranslate(x, 0f)
        // 画出游标.
        canvas.drawBitmap(slipBtnBmp, slipMatrix, paint)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {

        if (!isInterceptOn) {
            val old = isChoose
            when (event.action) {
                MotionEvent.ACTION_MOVE// 滑动
                -> nowX = event.x
                MotionEvent.ACTION_DOWN// 按下
                -> {
                    if (event.x > bgOnBmp.width || event.y > bgOnBmp.height) {
                        return false
                    }
                    onSlip = true
                    downX = event.x
                    nowX = downX
                }
                MotionEvent.ACTION_CANCEL // 移到控件外部
                -> {
                    onSlip = false
                    val choose = isChoose
                    if (nowX >= width / 2) {
                        nowX = width - slipBtnBmp.width * sx / 2
                        isChoose = true
                    } else {
                        nowX -= slipBtnBmp.width * sx / 2
                        isChoose = false
                    }
                    if (isChangeOn && choose != isChoose) { // 如果设置了监听器,就调用其方法..
                        onChangedListener?.OnChanged(this, isChoose)
                    }
                }
                MotionEvent.ACTION_UP// 松开
                -> {
                    onSlip = false
                    val lastChoose = isChoose
                    if (event.x >= width / 2) {
                        nowX = width - slipBtnBmp.width * sx / 2
                        isChoose = true
                    } else {
                        nowX -= width / 2
                        isChoose = false
                    }
                    if (lastChoose == isChoose) {// 相等表示点击状态未切换，之后切换状态
                        if (event.x >= width / 2) {
                            nowX = 0f
                            isChoose = false
                        } else {
                            nowX = width - slipBtnBmp.width * sx / 2
                            isChoose = true
                        }
                    }
                    // 如果设置了监听器,就调用其方法..
                    if (isChangeOn) {
                        onChangedListener?.OnChanged(this, isChoose)
                    }
                }
            }
            invalidate()// 重画控件
        }
        return true
    }


    fun setOnChangedListener(listener: OnChangedListener) {// 设置监听器,当状态修改的时候
        isChangeOn = true
        onChangedListener = listener
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        //设置透明度
        alpha = if (enabled) 1f else 0.5f
    }

    interface OnChangedListener {
        fun OnChanged(v: View, checkState: Boolean)
    }

    fun setInterceptState(isIntercept: Boolean) {// 设置监听器,是否在重画钱拦截事件,状态由false变true时 拦截事件
        isInterceptOn = isIntercept
        // onInterceptListener = listener;
    }
}

