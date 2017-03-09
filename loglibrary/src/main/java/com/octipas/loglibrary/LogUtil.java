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
    /**
     * delay in millisecond between each write of device info in logs
     * by default 1 hour
     */
    private int timerDeviceInfo = 3600000;

    /**
     * Constructor for logUtil class
     * @param ctx app context
     * @param file the log backup file
     * @param timerDeviceInfoTask delay in millisecond between each write of device info in logs
     */
    public LogUtil(Context ctx, File file, int timerDeviceInfoTask){
        logFile = file;
        mContext = ctx;
        BufferedReader reader = null;
        injectedCode = "";
        timerDeviceInfo = timerDeviceInfoTask;

        //Declare the timer
        Timer to = new Timer();
        //Set the schedule function and rate
        to.scheduleAtFixedRate(new LogDeviceInfoTask(), 0, timerDeviceInfo);

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

    /**
     * Sends logs to script php for the save in DB
     * Local logs file is deleted if insertion is done
     */
    public static void RegisterLogInDB(){
            // Instantiate the RequestQueue.
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            String url ="http://intranet.lukasbargoin.xyz/octipas/logLibrary/script/saveLog.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("OK")){
                                LogUtil.clearLogs();
                                Log.i("Info logs", "logs clear");
                            }
                            Log.d("responseListener", ""+response);
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

    /**
     * Read logs from files
     * @return a string containing logs
     */
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

    /**
     * Delete local logs file
     */
    public static void clearLogs(){
        logFile.delete();
    }

    /**
     *
     * @return a string containing js code to inject in the webView
     */
    public static String getInjectedCode() {
        return injectedCode;
    }

    /**
     *
     * @return local logs file
     */
    public static File getLogFile() {
        return logFile;
    }

    /**
     *
     * @return battery level in percent
     */
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

    /**
     *
     * @returna a double-type table containing device info
     */
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

    /**
     * Allow to modify the identifier of the phone/tablet
     * @param device_id phone/tablet identifier
     */
    public static void setDevice_id(String device_id) {
        LogUtil.device_id = device_id;
    }

    /**
     * Allow to modify the identifiant of the merchand.
     * @param merchand_id merchand identifier
     */
    public static void setMerchand_id(int merchand_id) {
        LogUtil.merchand_id = merchand_id;
    }

}
