package com.example.fox_04.tooldetectionkotlin

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by fox_04 on 2018/3/22.
 */
private var sToast: Toast? = null

fun showToast(context: Context, content: String) {
    if (sToast == null) {
        sToast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
    } else {
        sToast!!.setText(content)
    }
    sToast!!.show()
}

fun loge(tag: String, content: String?){
    Log.e(tag , content ?: tag)
}

fun logi(tag: String, content: String?){
    Log.i(tag , content ?: tag)
}