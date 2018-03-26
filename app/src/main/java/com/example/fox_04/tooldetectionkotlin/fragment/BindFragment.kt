package com.example.fox_04.tooldetectionkotlin.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.fox_04.tooldetectionkotlin.R
import com.example.fox_04.tooldetectionkotlin.logi
import com.example.fox_04.tooldetectionkotlin.presenter.MainPresenterImpl
import com.example.fox_04.tooldetectionkotlin.showToast
import com.example.fox_04.tooldetectionkotlin.thread.ClientThread
import com.example.fox_04.tooldetectionkotlin.utils.SharePreferencesUtil
import kotlinx.android.synthetic.main.fragment_binding.*
import java.lang.ref.WeakReference
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by fox_04 on 2018/3/24.
 */
class BindFragment : BaseFragment() , View.OnClickListener{
    val mPreferencesUtil : SharePreferencesUtil = SharePreferencesUtil.instance
    var curPos = 0

    companion object {
        fun instance(): BindFragment{
            return BindFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_binding, container, false);
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView(){
        btn_machine.setOnClickListener(this)
        btn_knife.setOnClickListener(this)
        btn_ensure.setOnClickListener(this)
        text_machine.setOnClickListener(this)
        text_knife.setOnClickListener(this)

        text_machine.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var textStr: String = text_machine.text.toString()
                if(textStr.endsWith("\n")){
                    textStr = textStr.substring(0 , textStr.length - 1)
                    text_machine.setText(textStr)
                    mMainPresenter!!.getCutterPosition(textStr)
                    text_machine.setSelection(textStr.length)
                }
            }

        })

        positionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                curPos = p2
            }

        }
    }


    override fun onClick(p0: View?) {
        val cncId = text_machine.text.toString()
        val toolId = text_knife.text.toString()
        when(p0){
            btn_machine ->{
                //此处应为先扫描二维码再获取网络数据
                mMainPresenter!!.getCutterPosition(cncId)
            }
            btn_knife -> {
                //待处理，扫描二维码
            }
            btn_ensure -> {
                if(TextUtils.isEmpty(mPreferencesUtil.getIpId()) || TextUtils.isEmpty(mPreferencesUtil.getPortId())) {
                    showWarnDialog("请先输入IP跟端口号")
                    return
                }
                mMainPresenter!!.getTeRecord(cncId , toolId , curPos)
                text_machine.isFocusableInTouchMode = false
                text_knife.isFocusableInTouchMode = false
            }
            text_machine -> {
                text_machine.isFocusableInTouchMode = true
                if(text_knife.isFocusableInTouchMode){
                    text_knife.isFocusableInTouchMode = false
                }
            }
            text_knife -> {
                text_knife.isFocusableInTouchMode = true
                if(text_machine.isFocusableInTouchMode){
                    text_machine.isFocusableInTouchMode = false
                }
            }
            else -> {}

        }

    }
    fun getSpinnerAdapter(list : List<String>): ArrayAdapter<String> {
        val adapter: ArrayAdapter<String> = ArrayAdapter(activity , android.R.layout.simple_spinner_item , list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    override fun setSpinner(posList: List<String>) {
        positionSpinner.adapter = getSpinnerAdapter(posList)
    }


    override fun sockerConn() {
        val ip = mPreferencesUtil.getIpId()
        val port = mPreferencesUtil.getPortId()!!.toInt()
        val data = mPreferencesUtil.getUploadDataId()
        logi("---socket--" , "--")

        val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
        val KEEP_ALIVE_TIME: Long = 1
        val KEEP_ALIVE_TIME_UTIT: TimeUnit  = TimeUnit.SECONDS
        val taskQueue = LinkedBlockingQueue<Runnable>()
        val executorService = ThreadPoolExecutor(NUMBER_OF_CORES , NUMBER_OF_CORES * 2 ,
                KEEP_ALIVE_TIME , KEEP_ALIVE_TIME_UTIT , taskQueue)
        executorService.execute(ClientThread(mHandler , ip?: "" , port , data?:"" , executorService))
    }

    override fun createPresenter(): MainPresenterImpl {
        return MainPresenterImpl(this)
    }

    private val mHandler = MyHandler(this)
    inner private class MyHandler(fragment: BindFragment) : Handler() {
        private val mFragment: WeakReference<BindFragment> = WeakReference(fragment)
        override fun handleMessage(msg: Message?) {
            if(mFragment.get() == null){
                return
            }
            when(msg!!.what){
              ClientThread.SOCKET_CODE -> {
                  val msgStr = msg.obj
                  if(msgStr.equals("OK")){
                      mPreferencesUtil.setUploadDataId("")
                      showSuccessDialog("发送数据给Socket成功")
                  }else{
                      showFailDialog("发送数据给Socket失败")
                  }
              }
                ClientThread.SOCKET_ERROR , ClientThread.SOCKET_ERROR ->{
                    showFailDialog("TCP/IP通信失败,请确认ip和端口号")
                }
                else ->{
                    showFailDialog("TCP/IP通信失败")
                }
            }
        }
    }

}