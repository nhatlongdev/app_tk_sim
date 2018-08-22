package com.example.computer.thongkesimcard.util;
/**
 * Created by Trang on 1/8/2016.
 */
public class StringUtil {

    /**
     * Check format of email
     * @param target is a email need checking
     *
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     *
     * Check field not empty and 6 word
     * @param input text input
     *
     */
    public static boolean isGoodField(String input){
        if (input == null || input.isEmpty() || input.length() < 6)
            return false;
        return true;
    }

    /**
     *
     * Check field not empty
     * @param input text input
     *
     */
    public static boolean isEmpty(String input){
        if (input == null || input.isEmpty())
            return true;
        return false;
    }

    /**
     *
     * get action of url request
     * @param url
     *
     */
    public static String getAction(String url){
        return url.substring(url.lastIndexOf("/")+1, url.length());
    }
}
