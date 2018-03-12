@echo off



rem set ANDROID_HOME = C:\Users\Marcel\AppData\Local\Android\

:preconditioncheck
if "%ANDROID_HOME%"=="" goto androidHomeNotSetError
if not exist %ANDROID_HOME%\platform-tools\adb.exe goto noAdbError
set ANDROID_HOME_DRIVE=%ANDROID_HOME:~0,2%
setlocal EnableDelayedExpansion
%ANDROID_HOME_DRIVE%
cd "%ANDROID_HOME%\platform-tools\"

:welcome
echo off
set /p input= Connect Nexus 5, wait for all Drivers to be installed
echo ... Listing ADB-Devices
adb devices
echo.

set /p input = If Device was found, hit enter
echo reboot to bootloader
adb reboot bootloader
echo.

echo Is bootloader unlocked^? (y/n)
set /p choice= 
if '%choice%' == 'y' goto bootloader_unlocked


set /p input = when in fastboot, hit enter but wait for drivers
echo Listing fastboot devices
fastboot devices
echo.

set /p input = If Device was found, hit enter
echo Unlocking Bootloader...
fastboot oem unlock 
echo.


echo reboot phone do standard settings, activate UBS-debug
set /p input=Hit enter when reactivated
echo reboot to bootloader
adb reboot bootloader
echo.

:bootloader_unlocked
set /p input = when in fastboot hit enter
echo Listing fastboot devices
fastboot devices
echo.

set /p input = If Device was found hit enter
echo flashing custom Recovery
fastboot flash recovery twrp-3.2.1-1-hammerhead.img
echo.


set /p input = start recovery and hit enter
echo Copy Lineage zip-file
adb push lineage-14.1-20180301-nightly-hammerhead-signed.zip /sdcard/
echo.

echo On Screen goto Wipe - Advanced Wipe 
echo Select Cache, System and Data partitions to be wiped and then Swipe to Wipe.
echo Go back to return to main menu, then select Install.
echo Navigate to /sdcard, and select the LineageOS .zip package.
echo Follow the on-screen prompts to install the package.
echo Select Reboot
echo select "Do not install"
pause

exit










:androidHomeNotSetError
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo Die Variable 'ANDROID_HOME' ist nicht gesetzt.
@echo Bitte wende dich an einen Betreuer.
@pause
exit 

:noAdbError
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo Das Tool adb ist nicht installiert.
@echo Bitte wende dich an einen Betreuer.
@pause
exit 