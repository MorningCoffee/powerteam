#!/bin/sh

service mysql start

nohup ruby server.rb &
echo $! > /run/powerteam-server.pid