#!/bin/bash

# Launch the compute server

pushd $(dirname $0) > /dev/null
basepath=$(pwd)
popd > /dev/null

cat << EndOfMessage
HELP: 
./server.sh

EndOfMessage

# Launch the compute server
java -cp "$basepath"/target/classes:"$basepath"/target/tp2.jar \
  -Djava.rmi.server.codebase=file:"$basepath"/target/tp2.jar \
  -Djava.security.policy="$basepath"/policy \
  com.inf8480_tp2.server.ComputeServerImpl $*
