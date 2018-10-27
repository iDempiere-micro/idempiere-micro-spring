CREATE TABLE adempiere.crm_category (
  crm_category_id numeric NOT null,
  crm_category_uu character varying(36) DEFAULT NULL::character varying,
  ad_client_id numeric NOT NULL,
  ad_org_id numeric NOT NULL,
  created timestamp without time zone DEFAULT statement_timestamp() NOT NULL,
  createdby numeric NOT NULL,
  isactive character(1) NOT NULL,
  name character varying(60) NOT NULL,
  updated timestamp without time zone DEFAULT statement_timestamp() NOT NULL,
  updatedby numeric NOT NULL,
  value character varying(60) NOT NULL
);


ALTER TABLE adempiere.crm_category OWNER TO adempiere;

--
-- Name: crm_customer_category; Type: TABLE; Schema: adempiere; Owner: adempiere
--

CREATE TABLE adempiere.crm_customer_category (
  crm_customer_category_id numeric NOT null,
  ad_client_id numeric NOT NULL,
  ad_org_id numeric NOT NULL,
  crm_customer_category_uu character varying(36) DEFAULT NULL::character varying,
  created timestamp without time zone DEFAULT statement_timestamp() NOT NULL,
  createdby numeric NOT NULL,
  isactive character(1) NOT NULL,
  name character varying(60) NOT NULL,
  updated timestamp without time zone DEFAULT statement_timestamp() NOT NULL,
  updatedby numeric NOT NULL,
  c_bpartner_id numeric NOT null references c_bpartner,
  crm_category_id numeric NOT NULL
);


ALTER TABLE adempiere.crm_customer_category OWNER TO adempiere;


ALTER TABLE ONLY adempiere.crm_category
  ADD CONSTRAINT crm_category_pkey PRIMARY KEY (crm_category_id);


--
-- Name: crm_customer_category crm_customer_category_pkey; Type: CONSTRAINT; Schema: adempiere; Owner: adempiere
--

ALTER TABLE ONLY adempiere.crm_customer_category
  ADD CONSTRAINT crm_customer_category_pkey PRIMARY KEY (crm_customer_category_id);

ALTER TABLE ONLY adempiere.crm_customer_category
  ADD CONSTRAINT crm_customer_category_bpartner FOREIGN KEY (c_bpartner_id) REFERENCES adempiere.c_bpartner(c_bpartner_id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY adempiere.crm_customer_category
  ADD CONSTRAINT crm_customer_category_category FOREIGN KEY (crm_category_id) REFERENCES adempiere.crm_category(crm_category_id) DEFERRABLE INITIALLY DEFERRED;
