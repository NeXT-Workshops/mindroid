@set startdir=%cd%
@cd ../../impl/androidApp
@call gradlew.bat javadoc
@cd ../ev3App
@call gradlew.bat javadoc
@cd ../serverApp
@call gradlew.bat javadoc
@cd ../include/ev3Messages
@call gradlew.bat javadoc
@cd %startdir%