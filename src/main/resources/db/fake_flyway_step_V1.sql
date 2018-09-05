-- Drop table

DROP TABLE IF EXISTS adempiere.flyway_schema_history;

CREATE TABLE adempiere.flyway_schema_history (
	installed_rank int4 NOT NULL,
	"version" varchar(50) NULL,
	description varchar(200) NOT NULL,
	"type" varchar(20) NOT NULL,
	script varchar(1000) NOT NULL,
	checksum int4 NULL,
	installed_by varchar(100) NOT NULL,
	installed_on timestamp NOT NULL DEFAULT now(),
	execution_time int4 NOT NULL,
	success bool NOT NULL,
	CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank)
);
CREATE INDEX flyway_schema_history_s_idx ON adempiere.flyway_schema_history USING btree (success);

INSERT INTO adempiere.flyway_schema_history (installed_rank,"version",description,"type",script,checksum,installed_by,installed_on,execution_time,success) VALUES 
(0,NULL,'<< Flyway Schema Creation >>','SCHEMA','"adempiere"',NULL,'adempiere','2018-08-30 23:01:20.813',0,true)
,(1,'1','iDempiere 5.1.0','SQL','V1__iDempiere_5.1.0.sql',-1553888761,'adempiere','2018-08-30 23:01:24.397',331898,true)
;