package com.example.fox_04.tooldetectionkotlin.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.fox_04.tooldetectionkotlin.MainActivity
import com.example.fox_04.tooldetectionkotlin.presenter.MainPresenterImpl
import com.example.fox_04.tooldetectionkotlin.view.IMainView

/**
 * Created by fox_04 on 2018/3/24.
 */
abstract class BaseFragment : Fragment(), IMainView{

    var mMainPresenter : MainPresenterImpl? = null
    var dialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainPresenter = createPresenter()
        mMainPresenter!!.attachView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog = SweetAlertDialog(activity , SweetAlertDialog.SUCCESS_TYPE)
        dialog?.setCancelable(false)
    }

    abstract fun createPresenter(): MainPresenterImpl

    override fun onDestroy() {
        super.onDestroy()
        mMainPresenter!!.detachView()
    }

    override fun showToast(msg: String) {
        com.example.fox_04.tooldetectionkotlin.showToast(activity, msg)
    }

    override fun showSuccessDialog(message: String) {
        dialog?.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog?.titleText = message
        dialog?.show()
    }

    override fun showFailDialog(message: String) {
        dialog?.changeAlertType(SweetAlertDialog.ERROR_TYPE)
        dialog?.titleText = message
        dialog?.show()
    }

    fun showWarnDialog(message: String){
        dialog?.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog?.titleText = message
        dialog?.show()
    }

    override fun sockerConn() {
    }
    override fun setSpinner(posList: List<String>) {
    }
}