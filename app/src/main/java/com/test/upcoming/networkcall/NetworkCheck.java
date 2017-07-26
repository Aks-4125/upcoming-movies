package com.test.upcoming.networkcall;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Aks4125 on 6/9/2017.
 */


public class NetworkCheck {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Context context;

    public NetworkCheck(Context ctx) {
        context = ctx;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }


    public boolean isConnected()

    {
        int conn = NetworkCheck.getConnectivityStatus(context);
        boolean status = true;
        if (conn == NetworkCheck.TYPE_WIFI) {
            status = true;//"Wifi enabled";
        } else if (conn == NetworkCheck.TYPE_MOBILE) {
            status = true;//"Mobile data enabled";
        } else if (conn == NetworkCheck.TYPE_NOT_CONNECTED) {
            status = false;//"Not connected to Internet";
        }
        return status;
    }
}
