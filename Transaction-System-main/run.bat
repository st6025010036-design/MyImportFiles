@echo off
chcp 65001 > nul
cd src
javac com/transaction/model/*.java com/transaction/service/*.java com/transaction/Main.java
java com.transaction.Main
pause