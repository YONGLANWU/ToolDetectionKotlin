package com.example.fox_04.tooldetectionkotlin.thread

import android.os.Handler
import android.os.Message
import com.example.fox_04.tooldetectionkotlin.logi
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.ExecutorService


/**
 * Created by fox_04 on 2018/3/24.
 */
class ClientThread(handler: Handler , ip: String , port: Int , data: String , executorService: ExecutorService): Runnable {
    var mHandler: Handler? = null
    var mIP = ""
    var mPort = 0
    var mData = ""
    var mExecutorService: ExecutorService? = null
    init {
        this.mHandler = handler
        this.mIP = ip
        this.mPort = port
        this.mData = data
        this.mExecutorService = executorService
    }
    companion object {
        @JvmField
        val SOCKET_CODE = 0x1233;
        @JvmField
        val SOCKET_CLOSE = 0x1234;
        @JvmField
        val SOCKET_ERROR = 0X1235;

        @JvmField
        val TIMEOUT = 3000;
    }
    override fun run() {
        try {
            var s = Socket()
            val address: InetSocketAddress = InetSocketAddress(mIP, mPort)
            s.connect(address, TIMEOUT)
            val input: InputStream = s.getInputStream()
            val output: OutputStream = s.getOutputStream()
            output.write(mData.toByteArray())

            logi("---thread--", "--")

            mExecutorService!!.execute(object : Runnable {
                override fun run() {
                    var content = ""
                    var count = 0
                    while (count == 0) {
                        count = input.available()
                    }

                    var b: ByteArray = kotlin.ByteArray(count)
                    input.read(b)
                    content = b.toString()

                    val msg = Message()
                    msg.what = SOCKET_CODE
                    msg.obj = content
                    mHandler!!.sendMessage(msg)

                    if (s != null) {
                        s.close()
                    }
                    if (output != null) {
                        output.close()
                    }
                    if (input != null) {
                        input.close()
                    }
                }
            })
        }catch(e : Exception){
            mHandler!!.sendEmptyMessage(SOCKET_ERROR)
        }
    }
}