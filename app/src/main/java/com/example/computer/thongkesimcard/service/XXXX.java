package com.example.computer.thongkesimcard.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
public class XXXX extends AccessibilityService {

    public static String TAG = "XXXX";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent");
        String text = event.getText().toString();
        Log.d("inForSIm", "TEXT TRA VE TU AccessibilityService:==>>" + text);

//        if (event.getClassName().equals("android.app.AlertDialog")) {
//            Log.d("inForSIm", "CHAY VAO DK IF" + text);
//            performGlobalAction(GLOBAL_ACTION_BACK);
//            Log.d("inForSIm","TH1: "  + text);
//            Intent intent = new Intent("com.times.ussd.action.REFRESH");
//            intent.putExtra("message", text);
//            // write a broad cast receiver and call sendbroadcast() from here, if you want to parse the message for balance, date
//            sendBroadcastResultText(text);
//        }

        performGlobalAction(GLOBAL_ACTION_BACK);
        Intent intent = new Intent("com.times.ussd.action.REFRESH");
        intent.putExtra("message", text);
        // write a broad cast receiver and call sendbroadcast() from here, if you want to parse the message for balance, date
        sendBroadcastResultText(text);
    }

    /*Send Broashcash*/
    public void sendBroadcastResultText(String text) {
        Intent intent = new Intent("RESULT_TEXT");
        intent.putExtra("result_text",text);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

}
