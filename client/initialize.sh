#!/bin/bash

cp powerteam_Bash_client.sh /etc/cron.d/powerteam_Bash_client.sh
cp powerteam_Ruby_client.rb /etc/cron.d/powerteam_Ruby_client.rb

cd /etc/cron.d/
rm powerteam 
echo 'SHELL=/bin/sh
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin

*/1 * 	* * * 	root	./powerteam_Bash_client.sh' >> powerteam

/etc/init.d/cron restart 
