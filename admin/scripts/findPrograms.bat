@echo off
@set startdir=%cd%

:run_finder
:: run implementationFinder
cd ..\..\impl\ImplementationFinder
call gradlew.bat run 1<nul 

@cd %startdir% 