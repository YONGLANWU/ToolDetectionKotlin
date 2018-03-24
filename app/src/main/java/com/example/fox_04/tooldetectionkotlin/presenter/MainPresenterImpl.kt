package com.example.fox_04.tooldetectionkotlin.presenter

import com.example.fox_04.tooldetectionkotlin.logi
import com.example.fox_04.tooldetectionkotlin.model.MainModel
import com.example.fox_04.tooldetectionkotlin.model.MainModelImpl
import com.example.fox_04.tooldetectionkotlin.showToast
import com.example.fox_04.tooldetectionkotlin.utils.SharePreferencesUtil
import com.example.fox_04.tooldetectionkotlin.view.IMainView
import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 * Created by fox_04 on 2018/3/23.
 */
class MainPresenterImpl(val mainView : IMainView) : MainPresenter {
    private val mMainModel : MainModel
    private val mPreferencesUtil : SharePreferencesUtil = SharePreferencesUtil.instance
    init {
        mMainModel = MainModelImpl()
    }

    var mViewRef : Reference<IMainView>? = null

    fun attachView(view : IMainView){
        mViewRef = WeakReference<IMainView>(view)
    }

    fun getView(): IMainView?{
        return mViewRef!!.get()
    }

    fun isViewAttached(): Boolean{
        return mViewRef != null && mViewRef?.get() != null
    }

    fun detachView(){
        mViewRef!!.clear()
        mViewRef = null
    }

    override fun getCutterPosition(machineId: String) {
        mMainModel.getCutterPosition(object: MainPresenter.OnGetListener{
            override fun getSuccess(list: List<String>) {
                if(isViewAttached()) {
                    mainView.setSpinner(list);
                }
            }

            override fun getFail(message: String?) {
                if(isViewAttached()) {
                    mainView.showFailDialog(message!!)
                }
            }
        } , machineId)
    }

    override fun getTeRecord(machineID: String, cutterId: String, position: Int) {
        mMainModel.getTeRecord(object: MainPresenter.OnGetListener{
            override fun getSuccess(list: List<String>) {
                mPreferencesUtil.setUploadDataId(list[0])
                ctCNCBinding(machineID , cutterId , position)
            }

            override fun getFail(message: String?) {
                if(isViewAttached())
                  mainView.showFailDialog(message!!)

            }

        } , machineID , cutterId , position)
    }

    override fun ctCNCBinding(machineID: String, cutterId: String, cutPos: Int) {
        mMainModel.ctCNCBinding(object: MainPresenter.OnGetListener{
            override fun getSuccess(list: List<String>) {
                if(isViewAttached())
                  mainView.sockerConn()
            }

            override fun getFail(message: String?) {
                if(isViewAttached())
                   mainView.showFailDialog("三体绑定失败:" + message)
            }

        },machineID , cutterId , cutPos)
    }

    override fun cuUnBind(cutterId: String) {
        mMainModel.cuUnBind(object: MainPresenter.OnGetListener{
            override fun getSuccess(list: List<String>) {
                if(isViewAttached())
                  mainView.showSuccessDialog("三体绑定:" + list[0])
            }

            override fun getFail(message: String?) {
                if(isViewAttached())
                  mainView.showToast(message!!)
            }

        } , cutterId)
    }
}