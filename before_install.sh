#!/bin/bash
rm -rf idempiere-docker-pgsql-5.1.0.latest.tar.gz
wget https://sourceforge.net/projects/idempiereksys/files/idempiere-docker-image/idempiere-docker-pgsql-5.1.0.latest.tar.gz
zcat idempiere-docker-pgsql-5.1.0.latest.tar.gz | docker load
docker volume create --name idempiere-pgsql-datastore
docker run -d -v idempiere-pgsql-datastore:/data -p 5433:5432 -e PASS="postgres" idempiere/idempiere-docker-pgsql:5.1.0.latest
