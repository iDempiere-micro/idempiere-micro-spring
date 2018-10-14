#!/bin/bash
set -e 
set -o pipefail
# start the application
make run &
# so we will allow it to take it's time
if [ ! -f dockerize-linux-amd64-v0.6.1.tar.gz ]; then
    wget https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz
fi
tar zxvf dockerize-linux-amd64-v0.6.1.tar.gz
./dockerize -wait http://localhost:8080/actuator/health -timeout 5m
cd integration_tests
npm i
./run.sh
pkill -f "java"
