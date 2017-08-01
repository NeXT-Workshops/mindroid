package mindroid.common.ev3.app;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;

public class Display {

    public static void showSystemIsReady(){
        LCD.clearDisplay();
        LocalEV3.get().getGraphicsLCD().setFont(Font.getLargeFont());
        LocalEV3.get().getGraphicsLCD().drawString("READY",40,50, GraphicsLCD.TOP);
        LocalEV3.get().getGraphicsLCD().setFont(Font.getSmallFont());
        LocalEV3.get().getGraphicsLCD().drawString("Waiting for connection.",25,90, GraphicsLCD.TOP);
    }

    public static void showSystemIsReadyAndConnected(){
        LCD.clearDisplay();
        LocalEV3.get().getGraphicsLCD().setFont(Font.getLargeFont());
        LocalEV3.get().getGraphicsLCD().drawString("READY",40,50, GraphicsLCD.TOP);
        LocalEV3.get().getGraphicsLCD().setFont(Font.getSmallFont());
        LocalEV3.get().getGraphicsLCD().drawString("Connection established!",25,90, GraphicsLCD.TOP);
    }

    public static void showPleaseWait(){
        LCD.clearDisplay();
        LocalEV3.get().getGraphicsLCD().setFont(Font.getDefaultFont());
        LocalEV3.get().getGraphicsLCD().drawString("PLEASE WAIT",30,50,GraphicsLCD.TOP);
    }
    

}
