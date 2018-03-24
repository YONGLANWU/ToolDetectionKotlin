package com.example.fox_04.tooldetectionkotlin.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.example.fox_04.tooldetectionkotlin.R
import com.example.fox_04.tooldetectionkotlin.presenter.MainPresenterImpl
import kotlinx.android.synthetic.main.fragment_un_binding.*

/**
 * Created by fox_04 on 2018/3/24.
 */
class UnbindFragment : BaseFragment() , OnClickListener{

    companion object {
        fun instance(): UnbindFragment{
            return UnbindFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_un_binding, container, false);
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView(){
        btn_knife.setOnClickListener(this)
        btn_unbind.setOnClickListener(this)
        text_knife.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val toolId = text_knife.text.toString().toUpperCase()
        when(p0){
            btn_knife ->{
                //处理二维码扫描的
            }
            btn_unbind ->{
                if(TextUtils.isEmpty(toolId)){
                    showWarnDialog("请先输入刀具编号")
                    return
                }
                mMainPresenter!!.cuUnBind(toolId)
                text_knife.isFocusableInTouchMode = false
            }
            text_knife ->{
                text_knife.isFocusableInTouchMode = true
            }
            else ->{}
        }
    }

    override fun createPresenter(): MainPresenterImpl {
        return MainPresenterImpl(this)    }
}