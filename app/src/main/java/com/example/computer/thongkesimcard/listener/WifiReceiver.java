package com.example.computer.thongkesimcard.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by phamv on 9/8/2016.
 */

public class WifiReceiver extends BroadcastReceiver {

    private static final String KEY_ACTION = "ACTION_NETWORK_STATE";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WifiReceiver","onReceive");
    }
}
