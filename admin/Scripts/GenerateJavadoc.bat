@set startdir=%cd%
@cd ../../impl/androidApp
@call gradlew.bat javadoc
REM @cd ../ev3App
REM @call gradlew.bat javadoc
REM @cd ../serverApp
REM @call gradlew.bat javadoc
REM @cd ../include/ev3Messages
REM @call gradlew.bat javadoc
@cd %startdir%
pause