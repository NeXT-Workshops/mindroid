@set startdir=%cd%
@call StartADBDaemon.bat 
@cd %startdir% 


@cd ../../impl/serverApp 
@call gradlew.bat run 
@cd %startdir% 
exit