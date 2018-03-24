package com.example.fox_04.tooldetectionkotlin.model

import com.example.fox_04.tooldetectionkotlin.presenter.MainPresenter

/**
 * Created by fox_04 on 2018/3/22.
 */
interface MainModel {

    fun getCutterPosition(onGetListener: MainPresenter.OnGetListener  , machineId: String)

    fun getTeRecord(onGetListener: MainPresenter.OnGetListener , machineID: String, cutterId: String, position: Int)

    fun ctCNCBinding(onGetListener: MainPresenter.OnGetListener , machineID: String, cutterId: String, position: Int)

    fun cuUnBind(onGetListener: MainPresenter.OnGetListener , cutterId: String)
}