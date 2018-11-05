/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server;

import java.awt.Desktop;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class Utils {

    public static void openWebpage(URI uri) {
        System.out.println("opening ");
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(URL url) {
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static InetAddress thisIp;

    private static String thisIpAddress;

    private static void setIpAdd() {
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            thisIpAddress = thisIp.getHostAddress().toString();
        } catch (Exception e) {
        }
    }

    public static String getIpAddress() {
        setIpAdd();
        return thisIpAddress;
    }

    public static String getAlpha(long num) {

        String result = "";
        while (num > 0) {
            num--; // 1 => a, not 0 => a
            long remainder = num % 26;
            char digit = (char) (remainder + 97);
            result = digit + result;
            num = (num - remainder) / 26;
        }

        return result;
    }
    
    public static Date getCurrentMonday() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c.getTime();
    }
}
