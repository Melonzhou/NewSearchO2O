package com.baidu.newsearch;

/**
 *
 * Created by Melon on 16/8/22.
 */
public class UtilHelper {
    public static boolean isNull(String text){
        if(text == null || text.length() <= 0 || text.trim().length() <= 0){
            return true;
        }
        return false;
    }

    public static boolean isNetOk(){
        return true;
    }


}
