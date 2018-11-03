#!/usr/bin/env bash

# Launch the repartitor

pushd $(dirname $0) > /dev/null
basepath=$(pwd)
popd > /dev/null

# Launch the name directory
java -cp "$basepath"/target/classes:"$basepath"/target/tp2.jar \
  -Djava.rmi.server.codebase=file:"$basepath"/target/tp2.jar \
  -Djava.security.policy="$basepath"/policy \
  com.inf8480_tp2.repartitor.Repartitor $*