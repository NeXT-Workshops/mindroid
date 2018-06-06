@rem ###############################################################################################
@rem
@rem This bat file checks whether an android phone is connected and proceeds
@rem to set up adb via Wifi.
@rem
@rem The location of the Android SDK is extracted from the env. var. ANDROID_HOME.
@rem
@rem Author: Felicia Ruppel
@rem  
@rem ###############################################################################################

:preconditioncheck
@if "%ANDROID_HOME%"=="" goto androidHomeNotSetError
@if not exist %ANDROID_HOME%\platform-tools\adb.exe goto noAdbError
@set ANDROID_HOME_DRIVE=%ANDROID_HOME:~0,2%
@setlocal EnableDelayedExpansion
@%ANDROID_HOME_DRIVE%
@cd "%ANDROID_HOME%\platform-tools\"

:welcome
@echo off
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@set /p input= Willkommen^^! Bitte verbinde das Android-Geraet mit dem Computer und druecke dann Enter: 
@set ip_address=0.0.0.0

:startConnection
@rem checking whether at least one device is attached
@set cmd="adb devices -l | findstr "device:" | find /C "device:""
@for /f %%a in ('!cmd!') do @set devices_attached=%%a

@if %devices_attached% GEQ 1 goto setupWifiConnection
@if %devices_attached% == 0 goto noDevice



:noDevice
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@set /p input= Es konnte kein Geraet gefunden werden. Stelle sicher, dass das Geraet verbunden ist, und druecke dann Enter: 
@goto startConnection


:setupWifiConnection
@echo Die Wifi-Verbindung wird gestartet...

@set cmd="adb -d shell ip route | findstr "wlan0" | findstr /r "[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*""
@rem ip address is the ninth token/"word":
@for /f "tokens=9" %%a in ('!cmd!') do @set ip_address=%%a
@if "%ip_address%" == "0.0.0.0" goto WifiError

@echo off
@adb -d tcpip 55555
@adb connect %ip_address%:55555
@if %ERRORLEVEL% == 1 goto WifiError

@set cmd="adb devices -l | findstr "192" | find /C "192""
@for /f %%a in ('!cmd!') do @set devices_attached_wifi=%%a
@if %devices_attached_wifi% == 0 goto WifiError
@echo Anzahl ueber Wifi verbundener Geraete: %devices_attached_wifi%
@goto FirstConnectionEstablished


:FirstConnectionEstablished
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo ^-^-^-
@echo Bitte noch nicht das Fenster schliessen
@echo ^-^-^-
@set /P input2='Schritt 1 ist geschafft^^! Folge nun den Anweisungen in der Anleitung... ^^(USB-Tehthering^^!^^)
@if /I "%input2%"=="ok" goto reconnecting
@goto FirstConnectionEstablished

:androidHomeNotSetError
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo Die Variable 'ANDROID_HOME' ist nicht gesetzt.
@echo Bitte wende dich an einen Betreuer.
@pause
exit 

:WifiError
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@set /p input= Es konnte keine Verbindung hergestellt werden. Ueberpruefe die Wifi-Verbindung des Android-Geraets und des PC und die USB-Verbindung und druecke Enter:
@goto setupWifiConnection

:reconnecting
@adb connect %ip_address%:55555
@if %ERRORLEVEL% == 1 goto reconnectError
@goto finished

:reconnectError
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo Es gab ein Problem beim Verbinden. Bitte stecke das Handy erneut an den Computer an.
goto startConnection

:noIP
@adb disconnect
goto startConnection

:noAdbError
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo Das Tool adb ist nicht installiert.
@echo Bitte wende dich an einen Betreuer.
@pause
exit 


:finished
@echo ^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-^-
@echo Das Verbinden war erfolgreich^^! Du kannst dieses Fenster schliessen.	
@pause
exit