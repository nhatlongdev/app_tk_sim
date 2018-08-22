package com.example.computer.thongkesimcard.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.computer.thongkesimcard.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppUtil {

    private static final String TAG = "AppUtil";

    ///////////////////////////////////////////////////////////////////////////
    // Start activity
    ///////////////////////////////////////////////////////////////////////////
    public static void startActivity(Context act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        act.startActivity(intent);
    }

    public static void startActivity(Context act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.putExtras(bundle);
        act.startActivity(intent);
    }

    public static void enableStrictMode() {
        // Allow strict mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static void startActivityLTR(Activity act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static void startActivityLTR(Activity act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static void startActivityRTL(Activity act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void startActivityRTL(Activity act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    public static boolean checkApi(int api) {
        return Build.VERSION.SDK_INT >= api;
    }

    // Request runtime permissions
    public static boolean checkPermission(Context ctx, String permission) {
        if (checkApi(Build.VERSION_CODES.M)) {
            return ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

public static String getDeviceName() {

    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;

    if (model.startsWith(manufacturer)) {
        return capitalize(model);
    } else {
        return capitalize(manufacturer) + " " + model;
    }
}

    public static String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

//    private static String getDeviceId() {
//        String deviceId = "";
//        final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (mTelephony.getDeviceId() != null) {
//            deviceId = mTelephony.getDeviceId();
//        } else {
//            deviceId = Settings.Secure.getString(.getContentResolver(), Settings.Secure.ANDROID_ID);
//        }
//        return deviceId;
//    }

    public static void getFacebookKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e(TAG, "Key hash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * Show hide keyboard
     */
    public static void showKeyboard(Context ctx, EditText editText) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * This method allow closing keyboard when users click out-side(out of edittext)
     *
     * @param act  context
     * @param view root view
     */
    public static void closeKeyboardWhenTouchOutside(final Activity act, View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(act);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                closeKeyboardWhenTouchOutside(act, innerView);
            }
        }
    }

    /**
     * Show toast notify.
     *
     * @param message can hard text or from string file
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, int message) {
        Toast.makeText(context, context.getString(message), Toast.LENGTH_LONG).show();
    }

    /**
     * Get color
     */
    public static int getColor(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static int convertPixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }

    public static int getScreenWidth(Activity act) {
        /* getWidth() is deprecated */
        // Display display = act.getWindowManager().getDefaultDisplay();
        // return display.getWidth();

        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static double getScreenWidthAsInch(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        return Math.sqrt(x + y);
    }

    /**
     * Add shortcut to home
     * @param context Context
     * @param tClass class is called(should be first class of app)
     *
     */
    public static void addShortcut(Context context, Class tClass) {
        //on Home screen
        Intent shortcutIntent = new Intent(context, tClass);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }

    public static void openWebView(Context context, String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }



}
