package com.example.fox_04.tooldetectionkotlin.presenter

/**
 * Created by fox_04 on 2018/3/22.
 */
interface MainPresenter {
    /**
     * 获取挂刀位置数据
     */
     fun getCutterPosition(machineId: String)


    /**
     * 获取刀具检测补正
     * @param machineID
     * @param cutterId
     * @param position
     */
     fun getTeRecord(machineID: String, cutterId: String, position: Int)

    /**
     * 三体绑定
     * @param machineID
     * @param cutterId
     */
     fun ctCNCBinding(machineID: String, cutterId: String, cutPos: Int)

     fun cuUnBind(cutterId: String)

    interface OnGetListener{
        fun getSuccess(list : List<String>)
        fun getFail(message: String?)
    }
}