package com.example.fox_04.tooldetectionkotlin.thread

import android.os.Handler
import android.os.Message
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket


/**
 * Created by fox_04 on 2018/3/24.
 */
class ClientThread(handler: Handler , ip: String , port: Int , data: String): Runnable {
    var mHandler: Handler? = null
    var mIP = ""
    var mPort = 0
    var mData = ""
    init {
        this.mHandler = handler
        this.mIP = ip
        this.mPort = port
        this.mData = data
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
        var s = Socket()
        val address: InetSocketAddress = InetSocketAddress(mIP , mPort)
        s.connect(address , TIMEOUT)
        val input: InputStream = s.getInputStream()
        val output: OutputStream = s.getOutputStream()
        output.write(mData.toByteArray())

        val thread:Thread = Thread(object: Runnable{
            override fun run() {
                var content = ""
                var count = 0
                while(count == 0){
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
        thread.start()

    }
}