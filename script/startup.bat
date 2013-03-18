@echo off
rem Licensed to the Apache Software Foundation (ASF) under one or more
rem contributor license agreements.  See the NOTICE file distributed with
rem this work for additional information regarding copyright ownership.
rem The ASF licenses this file to You under the Apache License, Version 2.0
rem (the "License"); you may not use this file except in compliance with
rem the License.  You may obtain a copy of the License at
rem
rem     http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.

if "%OS%" == "Windows_NT" setlocal
rem ---------------------------------------------------------------------------
rem Start script for {project.artifactId}-${project.version}
rem
rem $Id: startup.bat 562770 2007-08-04 22:13:58Z markt $
rem ---------------------------------------------------------------------------

rem Guess APP_HOME if not defined
set CURRENT_DIR=%cd%
if not "%APP_HOME%" == "" goto gotHome
set APP_HOME=%CURRENT_DIR%
if exist "%APP_HOME%\bin\${project.artifactId}-${project.version}.jar" goto okHome
cd ..
set APP_HOME=%cd%
cd %CURRENT_DIR%
:gotHome
if exist "%APP_HOME%\bin\${project.artifactId}-${project.version}.jar" goto okHome
echo The APP_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okHome

set EXECUTABLE=%APP_HOME%\bin\${project.artifactId}-${project.version}.jar

rem Check that target executable exists
if exist "%EXECUTABLE%" goto okExec
echo Cannot find %EXECUTABLE%
echo This file is needed to run this program
goto end
:okExec

rem Get remaining unshifted command line arguments and save them in the
set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:doneSetArgs

cd "%APP_HOME%\bin"
start "${project.artifactId}-${project.version}" java -Dfile.encoding=UTF-8 -XX:NewSize=256m -XX:MaxNewSize=256m -XX:SurvivorRatio=8 -Xms512m -Xmx512m -jar "%EXECUTABLE%" %CMD_LINE_ARGS%

:end
