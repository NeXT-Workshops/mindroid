@rem ###############################################################################################
@rem
@rem This bat file disconnects all smartphones connected via ADB.
@rem
@rem The location of the Android SDK is extracted from the env. var. ANDROID_HOME.
@rem
@rem Author: Roland Kluge
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
@set /p input= Willkommen^^! Wenn du alle gekoppelten Smartphones trennen willst, dann gebe 'ok' ein und druecke Enter: 
@if /I "%input%"=="ok" goto disconnecting
@echo Abbruch. Du kannst das Fenster jetzt schliessen.
@pause
goto end

:disconnecting
@adb disconnect
@echo Alle Smartphones erfolgeich getrennt. Du kannst das Fenster jetzt schliessen.
@pause

:end