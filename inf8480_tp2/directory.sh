#!/bin/bash

# Launch the server directory

pushd $(dirname $0) > /dev/null
basepath=$(pwd)
popd > /dev/null

cat << EndOfMessage
HELP: 
./directory.sh ip_address
	- ip_address: (OPTIONAL) L'addresse ip de l'annuaire 
	  Si l'arguement est non fourni, on conisdère que l'annuaire est local (ip_address = 127.0.0.1)

EndOfMessage

IPADDR=$1
if [ -z "$1" ]
  then
    IPADDR="127.0.0.1"
fi

# Launch the file server
java -cp "$basepath"/target/classes/:"$basepath"/target/classes/ \
  -Djava.rmi.server.codebase="$basepath"/target/classes/ \
  -Djava.security.policy="$basepath"/policy \
  -Djava.rmi.server.hostname="$IPADDR" \
  com.inf8480_tp2.directory.NameDirectoryImpl $IPADDR
