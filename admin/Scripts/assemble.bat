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
@rem ###############################################################################################

cd /D %~1
set ANDROID_HOME=%~2
set JAVA_HOME=%~3
call %ANDROID_HOME%\platform-tools\adb get-state

@echo off
if "%ERRORLEVEL%" == "0" goto installAndAssemble
goto assembleOnly

:installAndAssemble
@echo on
@if exist app\build.gradle (
FOR /F delims^=^"^ tokens^=2 %%G IN ('findstr /i "applicationId" app\build.gradle') do @set packageName=%%G
goto installAndOpen
)
call gradlew.bat installDebug --daemon 0<nul
exit

:installAndOpen
call gradlew.bat installDebug --daemon 0<nul &&  call %ANDROID_HOME%\platform-tools\adb shell monkey -p %packageName% -c android.intent.category.LAUNCHER 1
exit

:assembleOnly
@echo on
call gradlew.bat assembleDebug --daemon 0<nul
exit
