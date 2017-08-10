@rem ###############################################################################################
@rem
@rem  This bat file checks whether an android phone is connected and proceeds
@rem  to set up adb via Wifi.
@rem
@rem  The location of the Android SDK needs to be configured in the script.
@rem
@rem  
@rem ###############################################################################################

:preconditioncheck
@if "%ANDROID_HOME%"=="" goto androidHomeNotSetError

@rem @set ANDROID_HOME=C:\Users\luo\AppData\Local\Android\sdk
@set JAVA_HOME=%~3
@set ANDROID_HOME_DRIVE=%ANDROID_HOME:~0,2%
@setlocal EnableDelayedExpansion
@%ANDROID_HOME_DRIVE%
@cd "%ANDROID_HOME%\platform-tools\"
:welcome
@echo off
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@set /p input= Willkommen^^! Bitte verbinde das Android-Geraet mit dem Computer und druecke dann Enter: 
@set ip_address=0.0.0.0

:startConnection
@set cmd="adb devices -l | findstr "device:" | find /C "device:""
@for /f %%a in ('!cmd!') do @set devices_attached=%%a

@if %devices_attached% GEQ 1 goto checkWifiConnection
@if %devices_attached% == 0 goto noDevice


:checkWifiConnection
@set cmd="adb devices -l | findstr "192" | find /C "192""
@for /f %%a in ('!cmd!') do @set devices_attached_wifi=%%a
@if %devices_attached_wifi% == 0 goto setupWifiConnection
if %ip_address% == 0.0.0.0 goto noIP
@goto FirstConnectionEstablished


:noDevice
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@set /p input= Es konnte kein Geraet gefunden werden. Stelle sicher, dass das Ger„t verbunden ist, und druecke dann Enter: 
@goto startConnection


:setupWifiConnection
@echo Die Wifi-Verbindung wird gestartet...
@set cmd="adb shell ip route | findstr "wlan0" | findstr /r "[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*""
@for /f "tokens=3" %%a in ('!cmd!') do @set ip_address=%%a
@for /f "tokens=1  delims=/" %%a in ("%ip_address%") do @set ip_address=%%a
@if "%ip_address%" == "0.0.0.0" goto WifiError

@echo off
@adb tcpip 55555
@adb connect %ip_address%:55555
@if %ERRORLEVEL% == 1 goto WifiError
@goto FirstConnectionEstablished

:FirstConnectionEstablished
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo ^-^-^-
@echo Bitte noch nicht das Fenster schliessen
@echo ^-^-^-
@set /P input2='Schritt 1 ist geschafft^^! Folge nun den Anweisungen in der Anleitung.
@if /I "%input2%"=="ok" goto reconnecting
@goto FirstConnectionEstablished

:androidHomeNotSetError
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo Die Variable 'ANDROID_HOME' ist nicht gesetzt.
@echo Bitte wende dich an einen Betreuer.
@pause
exit 

:WifiError
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@set /p input= Die IP-Adresse konnte nicht gefunden werden. Ueberpruefe die Wifi-Verbindung des Android-Geraets und druecke Enter:
@goto setupWifiConnection

:reconnecting
@adb connect %ip_address%:55555
@if %ERRORLEVEL% == 1 goto reconnectError
@goto finished

:reconnectError
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo Es gab ein Problem beim Verbinden. Bitte stecke das Handy erneut an den Computer an.
goto startConnection

:noIP
@adb disconnect
goto startConnection


:finished
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo Das Verbinden war erfolgreich^^! Du kannst dieses Fenster schlieáen.	
@pause
exit