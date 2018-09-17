-- CRM categories

-- TABLE
CREATE TABLE adempiere.crm_category();
CREATE TABLE adempiere.crm_customer_category();
-- COLUMN
ALTER TABLE adempiere.crm_category ADD COLUMN ad_client_id numeric NOT NULL;
ALTER TABLE adempiere.crm_category ADD COLUMN ad_org_id numeric NOT NULL;
ALTER TABLE adempiere.crm_category ADD COLUMN category_id numeric NOT NULL;
ALTER TABLE adempiere.crm_category ADD COLUMN category_uu character varying(36) DEFAULT NULL::character varying;
ALTER TABLE adempiere.crm_category ADD COLUMN created timestamp without time zone NOT NULL DEFAULT statement_timestamp();
ALTER TABLE adempiere.crm_category ADD COLUMN createdby numeric NOT NULL;
ALTER TABLE adempiere.crm_category ADD COLUMN isactive character NOT NULL;
ALTER TABLE adempiere.crm_category ADD COLUMN name character varying(60) NOT NULL;
ALTER TABLE adempiere.crm_category ADD COLUMN updated timestamp without time zone NOT NULL DEFAULT statement_timestamp();
ALTER TABLE adempiere.crm_category ADD COLUMN updatedby numeric NOT NULL;
ALTER TABLE adempiere.crm_category ADD COLUMN value character varying(60) NOT NULL;
ALTER TABLE adempiere.crm_customer_category ADD COLUMN c_bpartner_id numeric NOT NULL;
ALTER TABLE adempiere.crm_customer_category ADD COLUMN category_id numeric NOT NULL;
-- INDEX
CREATE UNIQUE INDEX crm_category_pkey ON adempiere.crm_category USING btree (category_id);
ALTER TABLE adempiere.crm_category ADD CONSTRAINT crm_category_pkey PRIMARY KEY USING INDEX crm_category_pkey; -- (1)
-- FOREIGN_KEY
ALTER TABLE adempiere.crm_customer_category ADD CONSTRAINT crm_customer_category_c_bpartner_id_fkey FOREIGN KEY (c_bpartner_id) REFERENCES adempiere.c_bpartner(c_bpartner_id);
ALTER TABLE adempiere.crm_customer_category ADD CONSTRAINT crm_customer_category_category_id_fkey FOREIGN KEY (category_id) REFERENCES adempiere.crm_category(category_id);
