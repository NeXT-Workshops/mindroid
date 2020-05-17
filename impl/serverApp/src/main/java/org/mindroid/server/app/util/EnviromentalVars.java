package org.mindroid.server.app.util;

public class EnviromentalVars {

    private static final String KEY_ANDROID_HOME = "ANDROID_HOME";

    public static boolean doesAndroidHomeExist(){
        return System.getenv(KEY_ANDROID_HOME) != null;
    }

}
