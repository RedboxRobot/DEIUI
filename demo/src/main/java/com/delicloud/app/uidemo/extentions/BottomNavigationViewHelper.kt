package com.delicloud.app.uidemo.extentions

import android.annotation.SuppressLint
import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * 去除该控件超过4个item时的夸张动画
 * upta: 请使用官方方法，xml设置:     app:labelVisibilityMode="labeled"
 */
@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView::class.java.getDeclaredField("shifting")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        (0 until menuView.childCount).forEach {
            (menuView.getChildAt(it) as BottomNavigationItemView).let { view ->
                view.setShifting(false)
                // set once again checked value, so view will be updated
                view.setChecked(view.itemData.isChecked)
            }
        }
    } catch (e: NoSuchFieldException) {
        Log.e("BNVHelper", "Unable to get shift mode field", e)
    } catch (e: IllegalAccessException) {
        Log.e("BNVHelper", "Unable to change value of shift mode", e)
    }
}

/**
 * BottomNavigationView 和 ViewPager 联动
 */
fun BottomNavigationView.setupWithViewPager(viewPager: ViewPager) {
    setOnNavigationItemSelectedListener { item ->
        (0 until menu.size()).forEach {
            if (item == menu.getItem(it)) {
                viewPager.setCurrentItem(it, false)
                return@setOnNavigationItemSelectedListener true
            }
        }
        false
    }
}

