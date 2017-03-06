package com.octipas.loglibrary;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lukas on 03/03/2017.
 */

public class WriteLogTask extends AsyncTask<String, Integer, String > {

        String FILENAME = "appLog.txt";
        File file = new File(Environment.getExternalStorageDirectory(), FILENAME);

    /**
     *
     * @return
     */
    public static String getCurrentTimeStamp(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        return currentDateTime;
    }

    @Override
    protected String doInBackground(String... str) {
        String enter = "\n";
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, true);
            writer.append(getCurrentTimeStamp()+" ");
            writer.append(str[0].toString());
            writer.append(enter);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
