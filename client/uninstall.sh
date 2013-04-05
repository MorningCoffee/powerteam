#!/bin/bash

cd /etc/cron.d/

sudo rm -f powerteam

if [ $? -eq 0 ]
then
	echo "powerteam - REMOVED"
else
	echo "ERROR: CAN'T DELETE FILE POWERTEAM"
	exit 1
fi

if [ $? -eq 0 ]
then
	echo "powerteam_Bash_client.sh - REMOVED"
else
	echo "ERROR: CAN'T DELETE FILE POWERTEAM_BASH_CLIENT.SH"
	exit 1
fi

if [ $? -eq 0 ]
then
	echo "powerteam_Ruby_client.rb - REMOVED"
else
	echo "ERROR: CAN'T DELETE FILE POWERTEAM_RUBY_CLIENT.RB"
	exit 1
fi

echo "UNINSTALL - SUCCESS"