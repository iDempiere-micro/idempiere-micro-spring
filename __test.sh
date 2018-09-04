#!/bin/bash
set -e 
set -o pipefail
make run &
cd integration_tests
npm i
./run.sh
pkill -f "java"
