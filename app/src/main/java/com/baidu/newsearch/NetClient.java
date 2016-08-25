package com.baidu.newsearch;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求相关
 * Created by Melon on 16/8/22.
 */
public class NetClient {


    public static final int TYPE_GET = 0;
    public static final int TYPE_POST = 1;

    private HashMap<String, String> mHeaders = null;
    private HashMap<String, String> mParams = null;
    //请求类型
    private int requestType;
    private String requestUrl;
    private INetlistener mNetListener;



    public NetClient(String requestUrl,INetlistener netListener){
        this.requestUrl = requestUrl;
        this.mNetListener = netListener;
        mParams = new HashMap<>();
        mParams.put(ParamsData.ParamsKey.APP_ID,ParamsData.ParamsDefaultValue.APP_ID);
        mParams.put(ParamsData.ParamsKey.TOKEN,ParamsData.ParamsDefaultValue.TOKEN);
    }
    public void setRequestType(int type){
        requestType = type;
    }
    /**
     * 添加参数
     * @param key
     * @param value
     */
    public void addParam(String key , String value){
        if(UtilHelper.isNull(key)){
            return;
        }
        if(mParams == null){
            mParams = new HashMap<>();
        }

        mParams.put(key,value);

    }

    /**
     * 添加header参数
     * @param key
     * @param value
     */
    public void addHeader(String key , String value){
        if(UtilHelper.isNull(key)){
            return;
        }
        if(mHeaders == null){
            mHeaders = new HashMap<>();
        }
        mHeaders.put(key,value);
    }



    public void getNetData(){
        if(requestType == TYPE_GET){
            getDataByGetMethod();
        }else{
            getDataByPostMethod();
        }
    }

    private String getParamsStr(){
        String params = null;
        StringBuilder sb = null;
        try{

            if(mParams != null && mParams.size() > 0){
                sb = new StringBuilder();
                int size = mParams.size();
                int i = 0;
                Iterator<Map.Entry<String, String>> iterator = mParams.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    if(i > 0){
                        sb.append("&");
                    }
                    sb.append(String.format("%s=%s", entry.getKey(), URLEncoder.encode(entry.getValue(), "utf-8")));

                    i++;
                }
//                String sign = getSignStr();
//                if(UtilHelper.isNull(sign) == false){
//                    sb.append("&");
//                    sb.append(ParamsData.ParamsKey.SIGN);
//                    sb.append("=");
//                    sb.append(sign);
//                }

                params = sb.toString();


            }
        }catch (Throwable th){
            th.printStackTrace();
        }

        return params;
    }

    private String getSignStr(){
        String signStr = null;
        String key = null;
        String value = null;
        try{
            List<Map.Entry<String,String>> list = sort();
            StringBuilder sb = new StringBuilder();
            if(list != null && list.size() > 0) {

                int size = list.size();
                for(int i = 0; i < size ; i++){
                    Map.Entry<String, String> entry = list.get(i);
                    if(entry != null){
                        key =  entry.getKey();
                        value =  entry.getValue();
                        if(key != null && !ParamsData.ParamsKey.LOG_ID.equalsIgnoreCase(key)){
                            sb.append(String.format("%s=%s", key, URLEncoder.encode(value, "utf-8")));

                        }
                    }
                }


            }
            String str = sb.toString();
            Log.e("melon","params after sort= " + str);
            signStr =  Md5.toMd5(str+ParamsData.ParamsDefaultValue.TOKEN);

        }catch (Throwable th){
            th.printStackTrace();
        }
        return signStr;
    }

    private List<Map.Entry<String,String>> sort(){
        if(mParams == null){
            return null;
        }
        List<Map.Entry<String,String>> aList = new LinkedList<Map.Entry<String,String>>(mParams.entrySet());
        Collections.sort(aList, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
                return lhs.getKey().compareToIgnoreCase(rhs.getKey());

            }
        });

        return aList;
//        if(mParams != null && mParams.size() > 0) {
//
//
//            Iterator<Map.Entry<String, String>> iterator = mParams.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, String> entry = iterator.next();
//                if(entry != null){
//                    Log.i("melon","key=" + entry.getKey() + "--value=" + entry.getValue());
//                }
//
//            }
//        }
    }



    private void getDataByGetMethod(){
        try{
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request request = null;
            Call call = null;
            Request.Builder builder = new Request.Builder();
            builder.header("Content-Type", "text/html; charset=utf-8");
            String paramsStr = getParamsStr();
            if(UtilHelper.isNull(paramsStr) == false){
                builder.url(requestUrl + "?" + paramsStr);
            }else{
                builder.url(requestUrl);
            }

            //add headers & cookies
            request = builder.build();
            call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if(mNetListener != null){
                        mNetListener.onError(-1,"error!");
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response == null){
                        //完蛋了
                        Log.i("melon","response == null");
                    }else{

                        String str = response.body().string();
                        Log.i("melon",""+ str);
                        if(mNetListener != null){
                            mNetListener.onSuccess(0,"",null);
                        }

                    }
                }

            });
        }catch (Throwable th){
            th.printStackTrace();
        }
    }

    private void getDataByPostMethod(){
        try{
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request request = null;
            Call call = null;
            FormBody.Builder builder = new FormBody.Builder();
            if(mParams != null && mParams.size() > 0){
                Iterator<Map.Entry<String, String>> iterator = mParams.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    builder.add(entry.getKey(),entry.getValue());
                }
                String sign = getSignStr();
                if(UtilHelper.isNull(sign) == false){
                    builder.add(ParamsData.ParamsKey.SIGN,sign);
                }

            }

            RequestBody body = builder.build();

            //add headers & cookies

            request = new Request.Builder().url(requestUrl).post(body).build();
            call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("melon","fail....");
                    if(mNetListener != null){
                        mNetListener.onError(-1,"error!");
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response == null){
                        //完蛋了
                    }else{

                        String str = response.body().toString();
                        Log.i("melon",""+ str);
                        if(mNetListener != null){
                            mNetListener.onSuccess(0,"",null);
                        }

                    }
                }

            });


        }catch (Throwable th){
            th.printStackTrace();
        }
    }
    public interface INetlistener {
        void onSuccess(int errorCode , String errStr,JSONObject jsonData);

        void onError(int errorCode, String errStr);
    }
}
