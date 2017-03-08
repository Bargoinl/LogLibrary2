package com.octipas.loglibrary;

import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

/**
 * Created by Lukas on 02/03/2017.
 */

public class LogWebChromeClient extends WebChromeClient {

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        String data = "[CONSOLE Error] ";
        data += consoleMessage.message() + " in " + consoleMessage.sourceId()
                + " (Line: " + consoleMessage.lineNumber() + ")";
        LogUtil.writeLog(data);
        return super.onConsoleMessage(consoleMessage);
    }
}
