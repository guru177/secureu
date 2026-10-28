@echo off
title SecureU Web Portal
echo ========================================================
echo Starting SecureU Browser Web Application...
echo ========================================================
echo.

:: Open default browser to the web port
start "" http://localhost:3000

:: Start Node server
node local-server.js

pause
