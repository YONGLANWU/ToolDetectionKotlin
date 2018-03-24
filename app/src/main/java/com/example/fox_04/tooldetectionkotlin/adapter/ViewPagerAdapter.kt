package com.example.fox_04.tooldetectionkotlin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by fox_04 on 2018/3/21.
 */
class ViewPagerAdapter(fm: FragmentManager, titles: Array<String>, fragmentList: List<Fragment>)
    : FragmentPagerAdapter(fm) {

    private var mFragmentList =  fragmentList
    private val mTitles = titles

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]

    }
}