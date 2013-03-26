#!/bin/bash

export LOG=$(git reflog show origin/master --date=local --since="yesterday")	#get reflog information

export USER=$(git config user.name)	

ruby powerteam_Ruby_client.rb

