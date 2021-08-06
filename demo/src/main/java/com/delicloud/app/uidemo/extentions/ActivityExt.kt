package com.delicloud.app.uidemo.extentions

import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.addFragment(layoutRes: Int, otherFragment: Fragment) {
    val fm = supportFragmentManager
    fm.beginTransaction()
        .add(layoutRes, otherFragment)
        .commit()
}

inline val Activity.contentView: View?
    get() = findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)