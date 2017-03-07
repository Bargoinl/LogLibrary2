package com.octipas.applog;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.octipas.loglibrary.LogUtil;
import com.octipas.loglibrary.LogWebChromeClient;
import com.octipas.loglibrary.LogWebViewClient;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    String FILENAME = "myLog.txt";
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        file = new File(Environment.getExternalStorageDirectory(), FILENAME);

        WebView myWebView = (WebView) findViewById(R.id.myWebView);

        myWebView.setWebContentsDebuggingEnabled(true);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        myWebView.addJavascriptInterface(new LogUtil(MainActivity.this, file), "injectedObject");

        /* -- JS Error Inject -- */
        myWebView.evaluateJavascript("javascript:helloworld",null);

        myWebView.setWebViewClient(new LogWebViewClient());
        myWebView.setWebChromeClient(new LogWebChromeClient());

        myWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LogUtil.RegisterLogInDB();
                Toast.makeText(MainActivity.this, "futur action d'envoi de log",Toast.LENGTH_LONG).show();
                return false;
            }
        });

        /* ---- URL tests ---- */
        //myWebView.loadUrl("http://google.fr");
        //myWebView.loadUrl("http://fghsdbfjgbfdsjkbgqjs.fr");
        //myWebView.loadUrl("https://fr.wikipedia.org/404");
        //myWebView.loadUrl("http://octipas.com/");
        //myWebView.loadUrl("http://seeninmovies.lukasbargoin.fr");
        //myWebView.loadUrl("http://lukasbargoin.xyz/web");
        myWebView.loadUrl("http://intranet.lukasbargoin.xyz/ajax.html");
        //myWebView.loadUrl("tout-debrid.net/index.php");
    }
}
