#!/bin/bash

docker stop $(docker ps -aq)
docker rm $(docker ps -aq)
docker run --name idempiere-postgres -d -p 5433:5432 -e POSTGRES_PASSWORD=postgres postgres:alpine

#dockerize
if [ ! -f dockerize-linux-amd64-v0.6.1.tar.gz ]; then
    wget https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz
fi
tar zxvf dockerize-linux-amd64-v0.6.1.tar.gz
./dockerize -wait tcp://localhost:5433 -timeout 1m
sleep 10
PGPASSWORD=postgres psql -h localhost -p 5433 -U postgres -d postgres -f ./src/main/resources/db/prepare.sql