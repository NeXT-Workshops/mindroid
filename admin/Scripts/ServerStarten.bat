@set startdir=%cd%
@cd ../../impl/serverApp
@call gradlew.bat run
@cd %startdir%