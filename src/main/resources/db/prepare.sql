CREATE ROLE adempiere SUPERUSER LOGIN PASSWORD 'adempiere';

CREATE DATABASE idempiere WITH TEMPLATE template0 OWNER adempiere;

ALTER ROLE adempiere SET search_path TO adempiere, pg_catalog;

