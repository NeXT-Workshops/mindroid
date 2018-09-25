@rem ###############################################################################################
@rem
@rem Improved Version, August 2018
@rem
@rem  This bat file checks whether an android phone is connected and proceeds
@rem  to either only assemble the apk or to assemble and install on device.
@rem
@rem  Expects the following format:
@rem  call assemble.bat "<projectDir>" "<sdkDir>" "<jdkDir>"
@rem  
@rem  projectDir  directory of main folder of the android project that contains gradlew.bat
@rem  sdkDir      directory of android sdk
@rem  jdkDir      directory of java jdk
@rem
@rem To use this script in conjunction with the JavaEditor, replace the file 'assemble.bat' in the
@rem root directory of the JavaEditor with this file.
@rem 
@rem ###############################################################################################
@set startdir=%cd%
@echo off

:set_paths
cd /D %~1
set ANDROID_HOME=%~2
set JAVA_HOME="%~3"

:run_finder
:: run implementationFinder and push programs.json
cd ..\ImplementationFinder
call gradlew.bat run > nul 

:push
:: push programs.json to all devices
SETLOCAL ENABLEDELAYEDEXPANSION
@FOR /F "tokens=1,2 skip=1" %%A IN ('%ANDROID_HOME%\platform-tools\adb devices') DO (
    @SET IS_DEV=%%B
if "!IS_DEV!" == "device" (
	@SET SERIAL=%%A
	@call %ANDROID_HOME%\platform-tools\adb -s !SERIAL! push programs.json /storage/emulated/0/Mindroid/programs.json > nul
)
)
@ENDLOCAL
cd %~1

:check_for_devices
@set devices_attached=0
@FOR /F "tokens=* skip=1 delims=" %%A in ('%ANDROID_HOME%\platform-tools\adb devices') do @set devices_attached=1
@if "%devices_attached%" == "1" goto installAndAssemble
@goto noDevices



:installAndAssemble

echo #############################################################
echo #               Verbundene Geräte gefunden!                 #
echo #                                                           # 

:: get appID for opening later
@if exist app\build.gradle (
FOR /F delims^=^"^ tokens^=2 %%G IN ('findstr /i "applicationId" app\build.gradle') do @set packageName=%%G
goto installAndOpen
)
call gradlew.bat installDebug --daemon --warn 0<nul
goto end 

:installAndOpen
echo #                                                           #
echo #                    Baue die App...                        #

:: Call gradle wrapper and write ouput to file
call gradlew.bat installDebug --daemon --warn 1<nul > compileOutput.txt

:: Check Output in file for "BUILD FAILED", if found, goto error
For /F %%K IN ('findstr /i /C:"BUILD FAILED" compileOutput.txt') do goto error

:open_app
echo #               Installation  abgeschlossen                 #
echo #                                                           #
echo #                                                           #
echo #              Öffne App auf allen Geräten...               #

:: open app on all devices
SETLOCAL ENABLEDELAYEDEXPANSION
@FOR /F "tokens=1,2 skip=1" %%A IN ('%ANDROID_HOME%\platform-tools\adb devices') DO (
    @SET IS_DEV=%%B
if "!IS_DEV!" == "device" (
	@SET SERIAL=%%A
	@call %ANDROID_HOME%\platform-tools\adb -s !SERIAL! shell monkey -p %packageName% -c android.intent.category.LAUNCHER 1 > nul
)
)
@ENDLOCAL

echo #              App auf allen Geräten geöffnet               #
echo #                     Abgeschlossen!                        #
echo #############################################################
goto end 


:noDevices
echo.
echo #############################################################
echo #                  Kein Gerät verbunden!                    #
echo #                                                           #
echo #  App wird gebaut, aber nicht auf dem Handy installiert!   #
echo #                                                           #
echo #  Bitte verbinde das Handy bevor du die App installierst!  # 
echo #                                                           #
echo #                    Baue die App...                        #

:: Call gradle wrapper and write ouput to file
call gradlew.bat assemble --daemon --warn 1<nul > compileOutput.txt

:: Check Output in file for "BUILD FAILED", if found, goto error
For /F %%K IN ('findstr /i /C:"BUILD FAILED" compileOutput.txt') do goto error
echo #                                                           #
echo #                     Abgeschlossen!                        #
echo #############################################################
goto end 

:error
:: compile error found
echo.
echo #                                                            #
echo #                 Fehler beim kompilieren                    #
echo #          Folgende(r) Fehler ist/sind aufgetreten:          # 
echo ##############################################################
echo. 

:: delete old file
IF EXIST filtered.txt (
	del filtered.txt
)

:: remove first part of error that is useless
:: append lines to filtered.txt until last line 'x error(s)' is found, then stop appending 
set bla=
FOR /F "usebackq skip=16 delims=" %%L IN ("compileOutput.txt") DO (
	IF NOT DEFINED bla ( 
		echo %%L>>filtered.txt 
	)
	FOR /F "tokens=2" %%R IN ("%%L") DO (
		IF "%%R"=="error" ( 
			set bla="true"
		)
		IF "%%R"=="errors" ( 
			set bla="true"
		)
	)
)

:: print filtered Error
echo.
type filtered.txt

:end


IF EXIST filtered.txt (
	del filtered.txt
)
IF EXIST compileOutput.txt (
	del compileOutput.txt
)
@cd %startdir% 
::exit