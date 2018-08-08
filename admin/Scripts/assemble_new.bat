@rem ###############################################################################################
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
echo Starte assemble.bat

:set_paths
cd /D %~1
set ANDROID_HOME=%~2
set JAVA_HOME=%~3

:check_for_devices
@set devices_attached=0
@FOR /F "tokens=* skip=1 delims=" %%A in ('%ANDROID_HOME%\platform-tools\adb devices') do @set devices_attached=1
@if "%devices_attached%" == "1" goto installAndAssemble
@goto assembleOnly


:installAndAssemble
::call test.bat

echo #############################################################
echo #               Verbundene Geräte gefunden!                 #
echo #                                                           # 
@if exist app\build.gradle (
FOR /F delims^=^"^ tokens^=2 %%G IN ('findstr /i "applicationId" app\build.gradle') do @set packageName=%%G
goto installAndOpen
)
call gradlew.bat installDebug --daemon --warn 0<nul
goto end 

:installAndOpen
echo #                                                           #
echo #                    Baue die App...                        #
echo #############################################################
echo.
echo.

echo calling
call gradlew.bat installDebug --daemon --warn 2<nul 
echo calling done

:open_app

echo #############################################################
echo #               Installation  abgeschlossen                 #
echo #                                                           #
echo #              Öffne App auf allen Geräten...               #

SETLOCAL ENABLEDELAYEDEXPANSION
@FOR /F "tokens=1,2 skip=1" %%A IN ('%ANDROID_HOME%\platform-tools\adb devices') DO (
    @SET IS_DEV=%%B
if "!IS_DEV!" == "device" (
	@SET SERIAL=%%A
	@call %ANDROID_HOME%\platform-tools\adb -s !SERIAL! shell monkey -p %packageName% -c android.intent.category.LAUNCHER 1
)
)
@ENDLOCAL

echo.
echo #              App auf allen Geräten geöffnet                #
echo #                                                            #
echo #                     Abgeschlossen!	                      #
echo ##############################################################
goto end 


:assembleOnly
@rem no devices attached
::call test.bat
echo.
echo ##############################################################
echo #                   Kein Gerät verbunden,                    #
echo #   bitte verbinde das Handy bevor du die App installierst   # 
echo ##############################################################
echo.   
goto end 
:: without device attached, no need to build
:: call gradlew.bat assembleDebug --daemon 0<nul


:end
@cd %startdir% 
::exit