#!/bin/bash 

cp powerteam_Ruby_client.rb /etc/cron.d/powerteam_Ruby_client.rb

cd /etc/cron.d/

rm powerteam powerteam_Bash_client.sh powerteam.log

echo 'SHELL=/bin/sh
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin

*/1 *	* * *	root	cd /etc/cron.d/ && ./powerteam_Bash_client.sh >> powerteam.log
' >> powerteam

echo '#!/bin/bash

cd '$2'
export LOG_HOST='$1'
export LOG_GIT_REPO='$2'
export LOG=$(git reflog show origin/master --date=local --since="yesterday")
export USER=$(git config user.name)	

cd /etc/cron.d/
ruby powerteam_Ruby_client.rb
echo "Finish shell: " $?  ' >> powerteam_Bash_client.sh

chmod +x powerteam_Bash_client.sh

/etc/init.d/cron restart 