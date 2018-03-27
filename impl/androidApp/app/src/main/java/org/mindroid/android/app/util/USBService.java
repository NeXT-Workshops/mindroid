package org.mindroid.android.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class USBService {

    /**
     * Checks if USB is connected
     * @param context
     * @return
     */
    public static boolean isUSBConnected(Context context){
        Intent intent = context.registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));

        return intent.getExtras().getBoolean("connected");
    }

    /**
     * Checks if Tethering is activated.
     * @param context
     * @return
     */
    public static boolean isTetheringActivated(Context context){
        Intent intent = context.registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        return (intent.getExtras().getBoolean("rndis"));
    }
}
