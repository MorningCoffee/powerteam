#!/bin/bash

#New

#echo "Enter user: "
#read LOG_USER
#export LOG_USER

echo "Enter host: "
read LOG_HOST
export LOG_HOST

echo "Enter path to the git repo: "
read LOG_GIT_REPO
export LOG_GIT_REPO


#New

export LOG=$(git reflog show origin/master --date=local)	#get reflog information

export USER=$(git config user.name)	

ruby powerteam_Ruby_client.rb

echo "Finish shell: " $?


#--since="yesterday"
