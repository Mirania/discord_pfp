@echo off
if "%~1"=="" (
	echo Drag an image to this script.
	echo The output will be sent to the Desktop.
	echo.
	PAUSE
) else (
	echo Working...
	imagepreview.jar %1
)