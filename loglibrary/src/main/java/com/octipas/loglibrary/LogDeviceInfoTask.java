package com.octipas.loglibrary;

import java.util.TimerTask;

/**
 * Created by Lukas on 07/03/2017.
 */

public class LogDeviceInfoTask extends TimerTask {

    @Override
    public void run() {
        double[] memInfo = LogUtil.getMemoryInfo();
        String deviceInfo = "[DEVICE INFO]: battery="+LogUtil.getBatteryLevel()
                +"% available_memory="+memInfo[0]+"Mo "
                +"total_memory="+memInfo[1]+"Mo "
                +" percent_available_memory"+memInfo[2]+"% ";
        LogUtil.writeLog(deviceInfo);
    }
}
