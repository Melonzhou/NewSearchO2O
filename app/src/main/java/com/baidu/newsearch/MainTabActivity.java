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
                String url = "bnuoapi.search.hotbrand";
                NetClient nc = new NetClient(NSConfig.SERVER3, new NetClient.INetlistener() {
                    @Override
                    public void onSuccess(int errorCode, String errStr, JSONObject jsonData) {

                    }

                    @Override
                    public void onError(int errorCode, String errStr) {

                    }
                });
                nc.setRequestType(NetClient.TYPE_GET);
                nc.addParam("provider_name", "keywords_poi");
                nc.addParam("city_id", "100010000");
                nc.addParam("keywords","小肥羊");
                nc.addParam("encoding","utf-8");
//                nc.addParam("type","nuomi_listing");
//                nc.addParam("tn","android");
//                nc.addParam("start_idx","0");
//                nc.addParam("timestamp",""+System.currentTimeMillis()/1000);
//                nc.addParam("districtId","0");
                nc.addParam("page_idx","0");
                nc.addParam("sort_type","0");
                nc.addParam("goods_per_page","20");
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
