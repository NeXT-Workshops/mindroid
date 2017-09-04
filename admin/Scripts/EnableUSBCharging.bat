REM (Re-)enables USB charing
REM See script file DisableUSBCharing.bat for more details
REM
REM Author: Roland Kluge
REM Thanks: https://android.stackexchange.com/a/169430
REM

%ANDROID_HOME%/platform-tools/adb shell dumpsys battery set usb 1