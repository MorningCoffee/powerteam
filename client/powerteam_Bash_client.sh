#!/bin/bash

git reflog show origin/master --date=local --since="yesterday" > log.txt	#execute command reflog

ruby powerteam_Ruby_client.rb
