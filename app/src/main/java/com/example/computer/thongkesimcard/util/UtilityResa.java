package com.example.computer.thongkesimcard.util;

import android.app.Activity;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Computer on 08/12/2017.
 */

public class UtilityResa extends Activity {
    private Activity activity;
    private static UtilityResa _instance = null;

    /*language*/
    private Locale mLocale;

    public static UtilityResa getInstance(Activity activity){
        if(_instance==null){
            _instance = new UtilityResa();
        }else if(activity!=null){
            _instance.setActivity(activity);
        }
        return _instance;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    public void seeet(){
        Log.d("language_current: ","co chay vao ham nay");
    }

}
