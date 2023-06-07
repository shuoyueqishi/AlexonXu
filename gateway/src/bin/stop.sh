#!/bin/sh

APP_NAME="gateway-1.0-SNAPSHOT.jar"

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

