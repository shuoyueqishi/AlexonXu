#!/bin/sh

APP_NAME="chat-gpt-1.0-SNAPSHOT.jar"

PID=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')
if [ -z "$PID" ]
then
  echo "$APP_NAME is not running."
else
  echo "Stopping $APP_NAME..."
  kill $PID
  sleep 5
  echo "$APP_NAME has been stopped."
fi

cd ../
chmod +x $APP_NAME
echo "begin to start $APP_NAME"
nohup java -Denv=prod -Dfile.encoding=utf-8 -Xms256m -Xmx256m -XX:+UseG1GC -XX:G1HeapRegionSize=1 -XX:G1HeapWastePercent=5 -XX:G1ReservePercent=10 -XX:InitiatingHeapOccupancyPercent=15 -XX:+HeapDumpOnOutOfMemoryError -jar $APP_NAME &
echo "$APP_NAME started!"
