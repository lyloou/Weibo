package com.lyloou.weibo.view;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.lyloou.weibo.R;

/**
 * Created by liliu on 2015/5/4.
 */
public class AuthActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authview);

        WebView authView = (WebView) findViewById(R.id.id_auth_wv);
        authView.getSettings().setJavaScriptEnabled(true);
        authView.loadUrl("http://www.baidu.com");
//        authView.addJavascriptInterface(new JavascriptInterface(), "Methods");
    }
}
