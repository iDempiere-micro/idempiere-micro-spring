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
	rm -rf ./src/main/resources/db/migration/V1__iDempiere_5.1.0.sql
	( cd ./src/main/resources/db/migration/; gunzip -k V1__iDempiere_5.1.0.sql.gz )
	-./mvnw flyway:migrate -Dflyway.url=jdbc:postgresql://localhost:5433/idempiere&ssl=true&sslmode=require
	./mvnw flyway:migrate -Dflyway.url=jdbc:postgresql://localhost:5433/idempiere&ssl=true&sslmode=require