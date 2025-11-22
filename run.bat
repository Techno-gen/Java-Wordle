@echo off
echo Compiling...
javac Wordle.java
IF %ERRORLEVEL% EQU 0 (
    echo Running...
    java Wordle
)