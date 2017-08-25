REM
REM This script disables charging via USB for the connected Android device
REM This is useful in situations where the Android device is connnected to another 
REM battery-powered device.
REM 
REM Author: Roland Kluge
REM Thanks: https://android.stackexchange.com/a/169430
REM

%ANDROID_HOME%/platform-tools/adb shell dumpsys battery set usb 0