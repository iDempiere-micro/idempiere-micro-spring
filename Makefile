clean:
	./mvnw clean

compile:
	./mvnw compile

test:
	./mvnw surefire:test

run:
	./mvnw spring-boot:run
		
install:
	./mvnw install

package:
	./mvnw package

migrate:	
	./mvnw flyway:migrate -Dflyway.url=jdbc:postgresql://localhost:5433/idempiere&ssl=true&sslmode=require
