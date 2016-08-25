package com.baidu.newsearch;

/**
 * 网络请求的公共参数
 * Created by Melon on 16/8/25.
 */
public class ParamsData {
    public static class ParamsKey{
        public static final String APP_ID = "appid";
        public static final String TOKEN = "token";
        public static final String SIGN = "sign";
        public static final String METHOD = "method";
        public static final String LOCATION = "location";
        public static final String CITY_ID = "city_id";
    }

    public static class ParamsDefaultValue{
        public static final String APP_ID = "NewSearch";
        public static final String TOKEN = "cb7882bc27b2ef29afbb3b68d79f24ae";
    }
}
