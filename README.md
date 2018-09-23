# idempiere-micro-spring

[![Travis CI Build Status](https://travis-ci.org/iDempiere-micro/idempiere-micro-spring.svg?branch=master)](https://travis-ci.org/iDempiere-micro/idempiere-micro-spring) [![CircleCI](https://circleci.com/gh/iDempiere-micro/idempiere-micro-spring/tree/master.svg?style=svg)](https://circleci.com/gh/iDempiere-micro/idempiere-micro-spring/tree/master)
[![Maintainability](https://api.codeclimate.com/v1/badges/279819f3dc54d47b941c/maintainability)](https://codeclimate.com/github/iDempiere-micro/idempiere-micro-spring/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/279819f3dc54d47b941c/test_coverage)](https://codeclimate.com/github/iDempiere-micro/idempiere-micro-spring/test_coverage)

 THE repository for iDempiere micro - Java + Kotlin Spring Boot back-end compatible with iDempiere 

## Continuous integration

### Travis CI

- can compile and package
- can run against [iDempiere 5.1 Docker PostgreSQL](https://github.com/longnan/ksys-idempiere-docker-pgsql-3.1)
- can start (boot)
- can serve REST API
- integration test JavaScript client in [Frisby.JS](https://www.frisbyjs.com/) can login, token works (who am I and list users)

### CircleCI

- can compile and package
- can create an empty iDempiere 5.1 database
- deploy to the staging server http://idempiere-micro-spring.staging.hsharp.software (e.g. http://idempiere-micro-spring.staging.hsharp.software/actuator)
