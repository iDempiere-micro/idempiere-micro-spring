#!/bin/bash
set -e 
set -o pipefail
# make tests
make test
# start the application
make run &
# so we will allow it to take it's time
sleep 120
cd integration_tests
npm i
./run.sh
pkill -f "java"
