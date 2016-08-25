package com.baidu.newsearch;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
        OkHttpClient mOkHttpClient = new OkHttpClient();
//        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
//        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        Request request = null;
        Call call = null;


        if(requestType == TYPE_GET){

            Request.Builder builder = new Request.Builder();
            builder.url(requestUrl);
            builder.header("Content-Type", "text/html; charset=utf-8");
            //add headers & cookies
            request = builder.build();


        }else{
            FormBody.Builder builder = new FormBody.Builder();
            if(mParams != null && mParams.size() > 0){
                Iterator<Map.Entry<String, String>> iterator = mParams.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    builder.add(entry.getKey(),entry.getValue());
                }

            }

            RequestBody body = builder.build();

            //add headers & cookies

            request = new Request.Builder().url(requestUrl).post(body).build();

        }
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
                }else{

                    String str = response.body().toString();
                    Log.i("melon",""+ str);
                    if(mNetListener != null){
                        mNetListener.onSuccess(0,"",null);
                    }

                }
            }

        });


    }


    public interface INetlistener {
        void onSuccess(int errorCode , String errStr,JSONObject jsonData);

        void onError(int errorCode, String errStr);
    }
}
