@echo off
@set startdir=%cd%

cd ..\..\impl\ImplementationFinder

:push
:: push programs.json to all devices
SETLOCAL ENABLEDELAYEDEXPANSION
@FOR /F "tokens=1,2 skip=1" %%A IN ('%ANDROID_HOME%\platform-tools\adb devices') DO (
    @SET IS_DEV=%%B
if "!IS_DEV!" == "device" (
	@SET SERIAL=%%A
	@call %ANDROID_HOME%\platform-tools\adb -s !SERIAL! push programs.json /storage/emulated/0/Mindroid/programs.json
)
)
@ENDLOCAL

@cd %startdir% 

IF "%ANDROID_HOME%"=="" (
	ECHO ANDROID_HOME IS NOT DEFINED
	ECHO NOTHING WAS DONE
	pause 
	)

