package com.octipas.applog;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.octipas.loglibrary.LogUtil;
import com.octipas.loglibrary.LogWebChromeClient;
import com.octipas.loglibrary.LogWebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = (WebView) findViewById(R.id.myWebView);

        myWebView.setWebContentsDebuggingEnabled(true);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        myWebView.addJavascriptInterface(new LogUtil(), "injectedObject");

        /* -- JS Error Inject -- */
        myWebView.evaluateJavascript("javascript:helloworld",null);

        myWebView.setWebViewClient(new LogWebViewClient());
        myWebView.setWebChromeClient(new LogWebChromeClient());

        /* ---- URL tests ---- */
        //myWebView.loadUrl("http://google.fr");
        //myWebView.loadUrl("http://fghsdbfjgbfdsjkbgqjs.fr");
        //myWebView.loadUrl("https://fr.wikipedia.org/404");
        //myWebView.loadUrl("http://octipas.com/");
        //myWebView.loadUrl("http://seeninmovies.lukasbargoin.fr");
        //myWebView.loadUrl("http://lukasbargoin.xyz/web");
        myWebView.loadUrl("http://intranet.lukasbargoin.xyz/ajax.html");
    }

}
