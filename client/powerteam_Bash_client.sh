#!/bin/bash

git reflog show origin/master --date=local > log.txt	#execute command reflog
ruby powerteam_Ruby_client.rb

