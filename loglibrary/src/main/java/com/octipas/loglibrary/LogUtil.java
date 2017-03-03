package com.octipas.loglibrary;

import android.os.AsyncTask;
import android.os.Environment;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Lukas on 02/03/2017.
 */

public class LogUtil {

    //private static File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + "appLog.txt");

    /*@JavascriptInterface
    public static void writeLog(String data){
        FileOutputStream output;
        try {
            output = new FileOutputStream(myFile,true);
            output.write((data+'\n').getBytes());
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @JavascriptInterface
    public static void writeLog(String data) {
        new WriteLogTask().execute(data);
    }




}
