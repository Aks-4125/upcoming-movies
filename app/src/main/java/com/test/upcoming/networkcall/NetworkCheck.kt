package com.test.upcoming.networkcall

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by Aks4125 on 6/9/2017.
 */


class NetworkCheck(private val context: Context) {


    //"Wifi enabled";
    //"Mobile data enabled";
    //"Not connected to Internet";
    val isConnected: Boolean
        get() {
            val conn = NetworkCheck.getConnectivityStatus(context)
            var status = true
            if (conn == NetworkCheck.TYPE_WIFI) {
                status = true
            } else if (conn == NetworkCheck.TYPE_MOBILE) {
                status = true
            } else if (conn == NetworkCheck.TYPE_NOT_CONNECTED) {
                status = false
            }
            return status
        }

    companion object {
        var TYPE_WIFI = 1
        var TYPE_MOBILE = 2
        var TYPE_NOT_CONNECTED = 0

        fun getConnectivityStatus(context: Context): Int {
            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI

                if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE
            }
            return TYPE_NOT_CONNECTED
        }
    }
}
