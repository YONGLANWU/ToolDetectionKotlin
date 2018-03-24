package com.example.fox_04.tooldetectionkotlin.webconn

import android.util.Log
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

/**
 * Created by fox_04 on 2018/3/22.
 */
class WebServiceUtils(serviceUrl : String) {
    val TIME_OUT_CONN: Int = 5000
    var namespace: String
    var mServiceUrl: String
    companion object {
        @JvmField
        val SERVICE_NAMESPACE = "http://tempuri.org/"
        @JvmField
        val SERVICE_LOCAL_NAMESPACE = "http://services.com/"
        //定義webservice提供服務的url
        @JvmField
        val SERVICE_URL = "http://10.105.107.18:8002/"

        @JvmField
        //根据机台编号获取机台需挂刀位置
        val GET_CNC_CUTTER_PT = "Get_CNCCutterPT"
        //刀具检测补正值
        @JvmField
        val GET_TE_RECORD = "Get_Te_Record"
        //三体绑定
        @JvmField
        val CT_CNC_BINDING_SAVE = "CT_CNC_BINDING_SAVE"
        //三体解绑
        @JvmField
        val CU_CNC_UNBIND = "CU_CNC_UnBind"
    }

    init {
        this.namespace = SERVICE_NAMESPACE
        this.mServiceUrl = serviceUrl
    }

    /**
     * 調用遠程webservice獲取數據
     * @param keyAndVal 需要傳入的參數
     * @param methodName 調用WebService的方法名
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getInfo(keyAndVal: HashMap<String, String>?, methodName: String): List<String> {
        //調用的方法
        //創建HttpTransportSE傳輸對象
        val ht = HttpTransportSE(mServiceUrl, TIME_OUT_CONN)

        /* final NtlmTransport ht = new NtlmTransport();
        ht.setCredentials(serviceUrl, USERNAME, PASSWORD, "","");
*/
        ht.debug = true
        // 使用SOAP1.2协议创建Envelop对象
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val soapObject = SoapObject(namespace, methodName)
        if (keyAndVal != null && keyAndVal.size > 0) {
            val keys = keyAndVal.keys
            for (s in keys) {
                soapObject.addProperty(s, keyAndVal[s])
            }
        }


        envelope.bodyOut = soapObject
        // 设置与.NET提供的webservice保持较好的兼容性
        envelope.dotNet = true
        envelope.implicitTypes = true
        // 使用Callable与Future来创建启动线程
        val future = FutureTask(Callable<List<String>> {
            val infoList = ArrayList<String>()
            // 调用webservice
            //try {
            var result: SoapObject? = null
            // List<HeaderProperty> llstHeadersProperty = new ArrayList<>();
            //llstHeadersProperty.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("administrator:foxconn.11".getBytes())));

            // ht.call(SERVICE_NAMESPACE + methodName, envelope , llstHeadersProperty);
            ht.call(namespace + methodName, envelope)

            if (envelope.getResponse() != null) {
                // 获取服务器响应返回的SOAP消息
                result = envelope.bodyIn as SoapObject
                //SoapObject detail = (SoapObject) result.getProperty(PB_NAME + "Result");
                // 解析服务器响应的SOAP消息
                for (i in 0 until result.getPropertyCount()) {
                    if (!result.getProperty(i).toString().equals("anyType{}")) {
                        infoList.add(result.getProperty(i).toString())
                        Log.i("--1---", infoList[i])
                    }
                }
                Log.i("--count---", "length:" + infoList.size)
                return@Callable infoList
            }
            null
        })
        Thread(future).start()

        try {
            return future.get()
        } catch (e: Exception) {
            e.printStackTrace()
            var msg = ""
            if (e.message!!.contains("java.net.SocketTimeoutException")) {
                msg = "連接服務器超時，請檢查網絡!"
            } else if (e.message!!.contains("java.net.UnknownHostException")) {
                msg = "未知服務器，請檢查配置!"
            } else {
                msg = "網絡/服務器好像出現了一點問題o(╯□╰)o"
            }
            throw Exception(msg)
        }

    }


}