package com.octipas.loglibrary;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

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

    @JavascriptInterface
    public void sendLog(){
        try {
            // Instantiate the RequestQueue.
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            String url ="http://http://intranet.lukasbargoin.xyz/logLibraryScript/saveLog.php";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("merchand_id", "1755");
            jsonBody.put("logs", "test");
            jsonBody.put("device_id", "toto");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
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
                    params.put("merchand_id","1755");
                    params.put("logs","[HTTP ERROR] POST");
                    params.put("device_id", "toto34");
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
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
