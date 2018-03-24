package com.example.fox_04.tooldetectionkotlin.view

/**
 * Created by fox_04 on 2018/3/23.
 */
interface IMainView {
    fun showToast(msg: String)

    fun setSpinner(posList: List<String>);

    fun showSuccessDialog(message: String)

    fun showFailDialog(message: String)

    fun sockerConn()
}