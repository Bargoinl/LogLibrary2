package com.octipas.loglibrary;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by Lukas on 02/03/2017.
 */

public class LogUtil {

    private static Context mContext;
    private static String injectedCode = null;
    private static File logFile;

    private static int merchand_id;
    private static String device_id;

    public LogUtil(Context ctx, File file){
        logFile = file;
        mContext = ctx;
        BufferedReader reader = null;
        injectedCode = "";

        //Declare the timer
        Timer to = new Timer();
        //Set the schedule function and rate
        to.scheduleAtFixedRate(new LogDeviceInfoTask(), 0, 3600000);

        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open("InjectCode.js"), "UTF-8"));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                injectedCode +=  mLine;
                injectedCode +=  "\n";
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

    public static void RegisterLogInDB(){
            // Instantiate the RequestQueue.
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            String url ="http://intranet.lukasbargoin.xyz/logLibraryScript/saveLog.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogUtil.clearLogs();
                            Log.d("responseListener", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ErrorResponse", ""+error);
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("merchand_id",String.valueOf(merchand_id));
                    params.put("logs",readLogs());
                    params.put("device_id", device_id);
                    return params;
                }
            };
            requestQueue.add(stringRequest);

    }

    @JavascriptInterface
    public static void writeLog(String data) {
        new WriteLogTask().execute(data);
    }

    public static String readLogs(){
        StringBuilder logs = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(logFile));
            String line;

            while ((line = br.readLine()) != null) {
                logs.append(line);
                logs.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Log.e("readLog",""+e.getMessage());
        }
        return logs.toString();
    }

    public static void clearLogs(){
        logFile.delete();
    }

    public static String getInjectedCode() {
        return injectedCode;
    }

    public static File getLogFile() {
        return logFile;
    }

    public static float getBatteryLevel() {
        Intent batteryIntent = mContext.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }
        return ((float)level / (float)scale) * 100.0f;
    }

    public static double[] getMemoryInfo(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMem = mi.availMem / 0x100000L;
        double totalMem = mi.totalMem / 0x100000L;
        double percentAvail = (mi.availMem / (double)mi.totalMem) * 100;

        double[] memInfo = {availableMem,totalMem,percentAvail};
        return memInfo;
    }

    public static void setDevice_id(String device_id) {
        LogUtil.device_id = device_id;
    }

    public static void setMerchand_id(int merchand_id) {
        LogUtil.merchand_id = merchand_id;
    }

}
