package com.octipas.loglibrary;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by Lukas on 02/03/2017.
 */

public class LogWebViewClient extends WebViewClient {

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        String data = "[HTTP Error] "+ request.getMethod()+ ": "
                + String.valueOf(errorResponse.getStatusCode())
                + " "+errorResponse.getReasonPhrase()
                + " in "+request.getUrl();
        LogUtil.writeLog(data);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String data = "[URL Visited] " +request.getUrl() ;
        LogUtil.writeLog(data);
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:(function() {"+ LogUtil.getInjectedCode() + "})()");
    }

}
