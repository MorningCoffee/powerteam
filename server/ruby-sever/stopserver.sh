#!/bin/sh 

PID=$(cat /run/powerteam-server.pid) 
kill -9 $PID

service mysql stop