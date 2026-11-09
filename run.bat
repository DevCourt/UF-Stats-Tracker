@echo off
title UF Players Status Launch

cd /d "%~dp0"

set "PATH=%~dp0javafx\bin;%PATH%"

java --enable-native-access=javafx.graphics --enable-native-access=ALL-UNNAMED --module-path "%~dp0javafx\lib" --add-modules javafx.controls -jar "%~dp0UFPlayersLaunch.jar"

pause