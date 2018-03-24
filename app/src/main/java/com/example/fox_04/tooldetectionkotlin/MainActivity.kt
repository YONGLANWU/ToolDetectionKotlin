package com.example.fox_04.tooldetectionkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import com.example.fox_04.tooldetectionkotlin.adapter.ViewPagerAdapter
import com.example.fox_04.tooldetectionkotlin.dialog.EditDialogFragment
import com.example.fox_04.tooldetectionkotlin.fragment.BindFragment
import com.example.fox_04.tooldetectionkotlin.fragment.UnbindFragment
import com.example.fox_04.tooldetectionkotlin.utils.SharePreferencesUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mPagerAdapter:  ViewPagerAdapter? = null
    val titles = arrayOf("刀具绑定", "刀具解绑")

    val mPreferencesUtil: SharePreferencesUtil = SharePreferencesUtil.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        //设置默认的ip跟port
        mPreferencesUtil.setIpId("10.10.10.1")
        mPreferencesUtil.setPortId("8080")

        initView()
    }

    /**
     * 初始化控件
     */
    fun initView(){
        tabLayout.tabMode = TabLayout.MODE_FIXED
        for(s in titles){
            tabLayout.addTab(tabLayout.newTab().setText(s))
        }

        tabLayout.setOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.setCurrentItem(tab?.position ?: 0)
            }

        })
        var mFragmentList = listOf<Fragment>(BindFragment.instance() ,UnbindFragment.instance())

        mPagerAdapter = ViewPagerAdapter(supportFragmentManager , titles , mFragmentList)
        viewPager.adapter = mPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_set ->
                showEditDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showEditDialog(){
        val dialog = EditDialogFragment()
        dialog.show(fragmentManager , "EditDialog")
    }


}
