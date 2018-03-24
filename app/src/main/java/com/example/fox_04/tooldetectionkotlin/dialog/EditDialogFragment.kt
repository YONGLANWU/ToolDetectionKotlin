package com.example.fox_04.tooldetectionkotlin.dialog

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.fox_04.tooldetectionkotlin.R
import com.example.fox_04.tooldetectionkotlin.logi
import com.example.fox_04.tooldetectionkotlin.utils.SharePreferencesUtil
import kotlinx.android.synthetic.main.layout_dialog_enter.view.*

/**
 * Created by fox_04 on 2018/3/23.
 */
class EditDialogFragment : DialogFragment() , View.OnClickListener {

    private val mPreferencesUtil: SharePreferencesUtil = SharePreferencesUtil.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //1 通过样式定义
        setStyle(DialogFragment.STYLE_NORMAL,R.style.exitDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // /3 在此处设置 无标题 对话框背景色
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // //对话框背景色
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
        //getDialog().getWindow().setDimAmount(0.5f);//背景黑暗度

        //不能在此处设置style
        // setStyle(DialogFragment.STYLE_NORMAL,R.style.Mdialog);//在此处设置主题样式不起作用
        val view = inflater!!.inflate(R.layout.layout_dialog_enter , container , false)
        view.ensureBtn.setOnClickListener(this)
        view.cancelBtn.setOnClickListener(this)
        return view
    }

    override fun onClick(p0: View?) {
        when(p0){
            view.ensureBtn -> {
                val ip = view.ipEt.text.toString();
                val port = view.portEt.text.toString()
                logi("----dialog--" , "--" + ip)
                mPreferencesUtil.setIpId(ip)
                mPreferencesUtil.setPortId(port)
                dialog.dismiss()
            }
            view.cancelBtn ->{
                dialog.dismiss()
            }
        }
    }
}