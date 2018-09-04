# idempiere-micro-spring

[![Build Status](https://travis-ci.org/iDempiere-micro/idempiere-micro-spring.svg?branch=master)](https://travis-ci.org/iDempiere-micro/idempiere-micro-spring)

 THE repository for iDempiere micro - Java + Kotlin Spring Boot back-end compatible with iDempiere 

## Continous integration

### Travis CI

- can compile and package
- can run against [iDempiere 5.1 Docker PostgreSQL](https://github.com/longnan/ksys-idempiere-docker-pgsql-3.1)
- can start (boot)
- can serve REST API
- integration test JavaScript client in [Frisby.JS](https://www.frisbyjs.com/) can login

### CircleCI

- can compile and package
- can create an empty iDempiere 5.1 database
