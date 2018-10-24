# idempiere-micro-spring

[![CircleCI](https://circleci.com/gh/iDempiere-micro/idempiere-micro-spring/tree/master.svg?style=svg)](https://circleci.com/gh/iDempiere-micro/idempiere-micro-spring/tree/master)
[![Maintainability](https://api.codeclimate.com/v1/badges/279819f3dc54d47b941c/maintainability)](https://codeclimate.com/github/iDempiere-micro/idempiere-micro-spring/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/279819f3dc54d47b941c/test_coverage)](https://codeclimate.com/github/iDempiere-micro/idempiere-micro-spring/test_coverage)

 THE repository for iDempiere micro - Java + Kotlin Spring Boot back-end compatible with iDempiere 

## Continuous integration

### CircleCI

- can compile and package
- can create an empty iDempiere 5.1 database
- deploy to the staging server http://idempiere-micro-spring.staging-aws.hsharp.software (e.g. http://idempiere-micro-spring.staging-aws.hsharp.software/actuator)
- can start (boot)
- can serve REST API
- integration test JavaScript client in [Frisby.JS](https://www.frisbyjs.com/) can login, token works (who am I and list users)

For more information about the build process, visit the [iDempiere micro Spring Maven build generated site](https://idempiere-micro.github.io/idempiere-micro-spring-site/).
