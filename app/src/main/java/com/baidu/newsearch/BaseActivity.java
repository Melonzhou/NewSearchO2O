package com.baidu.newsearch;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Melon on 16/8/22.
 */
public class BaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
