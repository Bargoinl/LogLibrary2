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
import java.util.Timer;
import java.util.TimerTask;

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
        LogUtil.setDevice_id("12ab342");
        LogUtil.setMerchand_id(1755);

        /* -- JS Error Inject -- */
        myWebView.evaluateJavascript("javascript:helloworld",null);

        myWebView.setWebViewClient(new LogWebViewClient());
        myWebView.setWebChromeClient(new LogWebChromeClient());
        Log.d("battery", ""+LogUtil.getBatteryLevel());
        double[] test = LogUtil.getMemoryInfo();
        Log.d("memory"," "+test[0]+" "+test[1]+" "+test[2]);

        myWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LogUtil.RegisterLogInDB();
                Toast.makeText(MainActivity.this, "Logs envoyés",Toast.LENGTH_LONG).show();
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

        //Declare the timer
        Timer to = new Timer();
        //Set the schedule function and rate
        to.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                double[] memInfo = LogUtil.getMemoryInfo();
                String deviceInfo = "[DEVICE INFO]: battery="+LogUtil.getBatteryLevel()
                        +"% available_memory="+memInfo[0]+"Mo "
                        +"total_memory="+memInfo[1]+"Mo "
                        +" percent_available_memory"+memInfo[2]+"% ";
                LogUtil.writeLog(deviceInfo);
                //Called each time when 1000 milliseconds (1 second) (the period parameter)
                //put your code here
            }

        },
        //Set how long before to start calling the TimerTask (in milliseconds)
                0,
        //Set the amount of time between each execution (in milliseconds)
                60000*60);

    }


}
