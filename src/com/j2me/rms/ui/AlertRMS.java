/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.rms.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 *
 * @author willian
 */
public class AlertRMS {

    private static Alert alert;
    private static String textAlert =
            "There is no more space for data storage.";
    private static Display displayApp;
    private static Displayable current;

    public static void setDisplayApp(Display displayApp) {
        AlertRMS.displayApp = displayApp;
    }

    public static void setTextAlert(String textAlert) {
        AlertRMS.textAlert = textAlert;
    }

    public static void setTextAlertException(String textAlert) {
        AlertRMS.textAlert += textAlert;
    }

    public static void show() {
        if (alert == null) {
            alert = new Alert("Data Storage");
        }

        alert.setString(textAlert);
        alert.setType(AlertType.ERROR);

        current = displayApp.getCurrent();

        displayApp.setCurrent(alert, current);
    }
}
