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

cd /D %~1
set ANDROID_HOME=%~2
set JAVA_HOME=%~3
@set devices_attached=0
@FOR /F "tokens=* skip=1 delims=" %%A in ('%ANDROID_HOME%\platform-tools\adb devices') do @set devices_attached=1
@echo off
@if "%devices_attached%" == "1" goto installAndAssemble
@goto assembleOnly

:installAndAssemble
@echo on
@if exist app\build.gradle (
FOR /F delims^=^"^ tokens^=2 %%G IN ('findstr /i "applicationId" app\build.gradle') do @set packageName=%%G
goto installAndOpen
)
call gradlew.bat installDebug --daemon 0<nul
exit

:installAndOpen
call gradlew.bat installDebug --daemon 0<nul
@echo off
@SETLOCAL ENABLEDELAYEDEXPANSION 
:: OPEN ON ALL ATTACHED DEVICES ::
@FOR /F "tokens=1,2 skip=1" %%A IN ('%ANDROID_HOME%\platform-tools\adb devices') DO (
    @SET IS_DEV=%%B
if "!IS_DEV!" == "device" (
   @SET SERIAL=%%A
   @call %ANDROID_HOME%\platform-tools\adb -s !SERIAL! shell monkey -p %packageName% -c android.intent.category.LAUNCHER 1
)
)
@ENDLOCAL
exit

:assembleOnly
@echo on
call gradlew.bat assembleDebug --daemon 0<nul
exit

