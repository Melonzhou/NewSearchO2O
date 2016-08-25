package com.baidu.newsearch;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;


public class MainTabActivity extends BaseActivity {

    private TextView btn;
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == btn){
                String url = "http://www.baidu.com/";
                NetClient nc = new NetClient(url, new NetClient.INetlistener() {
                    @Override
                    public void onSuccess(int errorCode, String errStr, JSONObject jsonData) {

                    }

                    @Override
                    public void onError(int errorCode, String errStr) {

                    }
                });
                nc.setRequestType(NetClient.TYPE_GET);
                nc.getNetData();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_activity_layout);
        btn = (TextView) findViewById(R.id.view_btn);
        btn.setOnClickListener(mClickListener);
    }
}
