#!/bin/sh

APP_NAME="edoc-1.0-SNAPSHOT.jar"
cd ../
chmod +x $APP_NAME
echo "begin to start $APP_NAME"
nohup java -Dfile.encoding=utf-8 -Xms256m -Xmx256m -XX:+UseG1GC -XX:G1HeapRegionSize=1 -XX:G1HeapWastePercent=5 -XX:G1ReservePercent=10 -XX:InitiatingHeapOccupancyPercent=15 -XX:+HeapDumpOnOutOfMemoryError -jar $APP_NAME &
echo "$APP_NAME started!"
