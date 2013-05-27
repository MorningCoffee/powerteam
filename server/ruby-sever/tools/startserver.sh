#!/bin/sh

service mysql start

cd ..
nohup java -jar bin/powerteam-server-jar-with-dependencies.jar &
echo $! > /run/powerteam-server.pid