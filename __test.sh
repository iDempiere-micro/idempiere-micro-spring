#!/bin/bash
set -e 
set -o pipefail
# make tests
make test
# start the application
make run &
# so we will allow it to take it's time
./dockerize -wait http://localhost:8080/actuator/health -timeout 5m
cd integration_tests
npm i
./run.sh
pkill -f "java"
