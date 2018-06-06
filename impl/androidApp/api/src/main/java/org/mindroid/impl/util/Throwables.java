package org.mindroid.impl.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Throwables {

    public static String getStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        exception.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string

        try {
            sw.close();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sStackTrace;
    }
}
