#!/bin/bash


echo 'Insert path to the local reposetory: '
read repopath

cp powerteam_Ruby_client.rb /etc/cron.d/powerteam_Ruby_client.rb

cd /etc/cron.d/

rm powerteam
rm powerteam_Bash_client.sh

echo 'SHELL=/bin/sh
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin

*/1 * 	* * * 	root	cd /etc/cron.d/ && ./powerteam_Bash_client.sh' >> powerteam

echo '#!/bin/bash

cd '$repopath'

export LOG=$(git reflog show origin/master --date=local)	#get reflog information

export USER=$(git config user.name)	

cd /etc/cron.d/

ruby powerteam_Ruby_client.rb

echo "Finish shell: " $?


#--since="yesterday"' >> powerteam_Bash_client.sh

/etc/init.d/cron restart 
