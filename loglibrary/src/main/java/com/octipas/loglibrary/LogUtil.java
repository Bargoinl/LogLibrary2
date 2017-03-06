package com.octipas.loglibrary;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Lukas on 02/03/2017.
 */

public class LogUtil {

    private Context mContext;
    private static String injectedCode = null;
    private static File logFile;

    public LogUtil(Context ctx, File file){
        logFile = file;
        mContext = ctx;
        BufferedReader reader = null;
        injectedCode = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open("InjectCode.js"), "UTF-8"));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                injectedCode +=  mLine;
            }
        } catch (IOException e) {
            Log.e("tag",""+e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("tag",""+e.getMessage());
                }
            }
        }
    }

    public static String getInjectedCode() {
        return injectedCode;
    }

    public static File getLogFile() {
        return logFile;
    }

    @JavascriptInterface
    public static void writeLog(String data) {
        new WriteLogTask().execute(data);
    }

}
