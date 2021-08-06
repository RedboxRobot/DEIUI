package com.delicloud.app.uidemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *Created By Mr.m
 *2019/7/19
 **/
class SimpleFragmentPagerAdapter(fm: FragmentManager, val list: List<Fragment>, var titleList:List<String>?=null) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList?.get(position)
    }

}