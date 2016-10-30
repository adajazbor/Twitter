package com.ada.twitter.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by ada on 10/22/16.
 */
public class CheckConnectivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        boolean isConnected = arg1.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (isConnected) {
            Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
        }
    }
}
