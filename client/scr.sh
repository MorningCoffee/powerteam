#!/bin/bash

if test -t 1; then
	exec>log>&1
else 
	false
fi

OUTPUT=`git reflog show --date=local`
echo $OUTPUT
