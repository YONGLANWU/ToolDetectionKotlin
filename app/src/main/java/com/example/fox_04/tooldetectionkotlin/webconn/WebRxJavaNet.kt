package com.example.fox_04.tooldetectionkotlin.webconn

import io.reactivex.Observable

/**
 * Created by fox_04 on 2018/3/22.
 */
class WebRxJavaNet {
    companion object {
        fun callWebservice(params : HashMap<String , String> , methodName: String): Observable<List<String>>{
            return Observable.create{
                source ->
                    val infoList: List<String> = WebServiceUtils(WebServiceUtils.SERVICE_URL).getInfo(params, methodName)
                    source.onNext(infoList)
                }
        }
    }
}