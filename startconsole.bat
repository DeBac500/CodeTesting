@echo off
SET OUTPUT_FILE=logs/results.txt
echo starting robotrader engine...
echo results stored in file %OUTPUT_FILE%
java -cp dist/robotrader.jar;lib/log4j-1.2.12.jar;lib/nanoxml-2.2.3.jar;lib/jakarta-regexp-1.4.jar robotrader.ConsoleMain conf/quotes.xml conf/traders.xml -all > %OUTPUT_FILE%
echo robotrader engine stopped.
pause