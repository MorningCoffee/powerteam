#!/bin/bash

export LOG=$(git reflog show origin/master --date=local)	#get reflog information

export USER=$(git config user.name)	

ruby powerteam_Ruby_client.rb

echo "Finish shell: " $?


#--since="yesterday"
#111
