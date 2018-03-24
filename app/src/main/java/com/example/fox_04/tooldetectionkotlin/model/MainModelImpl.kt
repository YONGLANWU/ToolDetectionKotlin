package com.example.fox_04.tooldetectionkotlin.model

import com.example.fox_04.tooldetectionkotlin.loge
import com.example.fox_04.tooldetectionkotlin.logi
import com.example.fox_04.tooldetectionkotlin.presenter.MainPresenter
import com.example.fox_04.tooldetectionkotlin.utils.StringFormatUtil
import com.example.fox_04.tooldetectionkotlin.webconn.WebRxJavaNet
import com.example.fox_04.tooldetectionkotlin.webconn.WebServiceUtils
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by fox_04 on 2018/3/23.
 */
class MainModelImpl : MainModel {

    private var cutPosList: List<String>? = null;
    override fun getCutterPosition(onGetListener: MainPresenter.OnGetListener, machineId: String) {

        val mCPOnGetListener = onGetListener;
        val keyVal = hashMapOf("MachineID" to machineId)
        WebRxJavaNet.callWebservice(keyVal ,  WebServiceUtils.GET_CNC_CUTTER_PT).subscribe(
                {
                    result ->
                    if(result != null){
                        loge("--onNext--", result.get(0))
                        cutPosList = StringFormatUtil.parseStringFormat(result[0])
                        mCPOnGetListener.getSuccess(cutPosList!!)
                    }else{
                        mCPOnGetListener.getFail("返回数据为null")
                    }
                },{
                     error ->
                     logi("----rxjava--" , error.message)
                     mCPOnGetListener.getFail((error.message))
                }
        )
    }

    override fun getTeRecord(onGetListener: MainPresenter.OnGetListener, machineID: String, cutterId: String, position: Int){
        val mTROnGetListener = onGetListener;
        if(cutPosList == null || cutPosList!!.size == 0) return
        val curPos = cutPosList!![position];
        val keyVal  = hashMapOf("cncid" to machineID, "toolsn" to cutterId , "localid" to curPos)
        WebRxJavaNet.callWebservice(keyVal ,  WebServiceUtils.GET_TE_RECORD).subscribe({
            result ->
            if(result == null){
                mTROnGetListener.getFail("补正数据为空")
                return@subscribe
            }
            logi("--getTeRecord--", result[0])
            if(result[0].contains("ERR")){
                mTROnGetListener.getFail("刀具检测补正失败:" + result[0])
            }else{
                mTROnGetListener.getSuccess(result)
            }
        },{
            error ->
            loge("--getTeRecord--", error.toString())
            mTROnGetListener.getFail(error.message)
        })
    }

    override fun cuUnBind(onGetListener: MainPresenter.OnGetListener, cutterId: String) {
        val mCCBOnGetListener = onGetListener;
        val keyVal = hashMapOf( "toolsn" to cutterId)
        WebRxJavaNet.callWebservice(keyVal , WebServiceUtils.CU_CNC_UNBIND)
                .subscribe({
                    result ->
                        mCCBOnGetListener.getSuccess(result)
                },{
                    error ->
                    mCCBOnGetListener.getFail(error.message)
                })
    }

    override fun ctCNCBinding(onGetListener: MainPresenter.OnGetListener, machineID: String, cutterId: String, position: Int) {
        val mCCBOnGetListener = onGetListener;
        if(cutPosList == null || cutPosList!!.size == 0) return
        val curPos = cutPosList!![position];
        val keyVal = hashMapOf("cncid" to machineID, "toolsn" to cutterId , "localid" to curPos)
        WebRxJavaNet.callWebservice(keyVal , WebServiceUtils.CT_CNC_BINDING_SAVE)
                .subscribe({
                    result ->
                    if(result!![0].equals("OK")){
                        mCCBOnGetListener.getSuccess(result)
                    }else{
                        mCCBOnGetListener.getFail(result[0])
                    }
                },{
                    error ->
                    mCCBOnGetListener.getFail(error.message)
                })
    }
}