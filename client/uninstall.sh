#!/bin/bash

cd /etc/cron.d/

sudo rm -f powerteam
echo "powerteam - REMOVED"

sudo rm -f powerteam_Bash_client.sh
echo "powerteam_Bash_client.sh - REMOVED"

sudo rm -f powerteam_Ruby_client.rb
echo "powerteam_Ruby_client.rb - REMOVED"

echo "UNINSTALL - SUCCESS"