package com.example.fox_04.tooldetectionkotlin.utils

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.fox_04.tooldetectionkotlin.MyApplication

/**
 * Created by fox_04 on 2018/3/21.
 */
class SharePreferencesUtil {
    private var update_data_id: String? = null
    private var ip_id: String? = null
    private var port_id: String? = null
    private var preferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.instance());
        editor = preferences.edit()
    }

    companion object {
        @JvmField
        val UPLOAD_DATA_ID: String = "upload_data_id"
        @JvmField
        val IP_ID: String = "ipid"
        @JvmField
        val PORT_ID: String = "port_id"

        val instance by lazy {
            SharePreferencesUtil()
        }
    }

    fun getUploadDataId(): String?{
        update_data_id = null
        if (update_data_id == null) {
            update_data_id = preferences.getString(UPLOAD_DATA_ID, "")
        }
        return update_data_id
    }

    fun setUploadDataId(update_data_id: String) {
        if (editor.putString(UPLOAD_DATA_ID, update_data_id).commit()) {
            this.update_data_id = update_data_id
        }
    }

    fun getIpId(): String? {
        ip_id = null
        if (ip_id == null) {
            ip_id = preferences.getString(IP_ID, "")
        }
        return ip_id
    }

    fun setIpId(ip_id: String) {
        if (editor.putString(IP_ID, ip_id).commit()) {
            this.ip_id = ip_id
        }
    }

    fun getPortId(): String? {
        port_id = preferences.getString(PORT_ID, "")
        return port_id
    }

    fun setPortId(port_id: String) {
        if (editor.putString(PORT_ID, port_id).commit()) {
            this.port_id = port_id
        }
    }

}