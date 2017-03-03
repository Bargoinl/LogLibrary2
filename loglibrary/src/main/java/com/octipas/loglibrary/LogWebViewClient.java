package com.octipas.loglibrary;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Lukas on 02/03/2017.
 */

public class LogWebViewClient extends WebViewClient {

    final String jsInjectCode = "function parseForm(event) {"
            + "var form = this;"
            + "var inputs = form.getElementsByTagName('input');"
            + "var data = '[FORM REQUEST] '+ form.method.toUpperCase() +': [Data] ';"
            + "for (var i = 0; i < inputs.length; i++) {"
            + "     var field = inputs[i];"
            + "     if (field.type != 'submit'){"
            + "         data += field.name + '=' + field.value + ' ';"
            + "     };"
            + "}"
            + "injectedObject.writeLog(data);"
            + "}"
            + ""
            + "function parseAjaxData(data) {"
            + "     ajaxReq += ' [Data] ';"
            + "     inputs = data.split('&');"
            + "     for (var i = 0; i < inputs.length; i++) {"
            + "         ajaxReq += inputs[i]+' '; "
            + "     };"
            + "     injectedObject.writeLog(ajaxReq);"
            + "     ajaxReq = ''; "
            + "}"
            + ""
            + "var test = XMLHttpRequest.prototype.send;"
            + "XMLHttpRequest.prototype.send = function(data){ "
            + "     parseAjaxData(data);"
            + "     test(data);"
            + "};"
            + ""
            + "var ajaxReq = ''; "
            + "var reqOpen = XMLHttpRequest.prototype.open;"
            + "XMLHttpRequest.prototype.open = function(){ "
            + "     ajaxReq += '[AJAX REQUEST] '+arguments[0]+ ' : ';"
            + "     ajaxReq += '[TARGET] '+arguments[1];"
            + "     return reqOpen.apply(this, [].slice.call(arguments));"
            + "};"
            + ""
            + "for (var form_idx = 0; form_idx < document.forms.length; ++form_idx)"
            + "    document.forms[form_idx].addEventListener('submit', parseForm, true);";

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        String data = "[HTTP Error] "+ request.getMethod()+ ":"
                + String.valueOf(errorResponse.getStatusCode())
                + " "+errorResponse.getReasonPhrase()
                + " in "+request.getUrl();
        LogUtil.writeLog(data);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String data = "[URL Visited]" +request.getUrl() ;
        LogUtil.writeLog(data);
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:(function() {"+ jsInjectCode + "})()");
    }

}
