INSERT INTO adempiere.ad_table
(ad_table_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, 
help, tablename, isview, accesslevel, entitytype, ad_window_id, ad_val_rule_id, loadseq, issecurityenabled, 
isdeleteable, ishighvolume, importtable, ischangelog, replicationtype, po_window_id, copycolumnsfromtable, iscentrallymaintained, ad_table_uu, processing, 
databaseviewdrop, copycomponentsfromview)
SELECT MAX(ad_table_id)+1, 0, 0, 'Y'::bpchar, now(), 0, now(), 0, 
'crm_category', -- name
'CRM Category',  -- description
'', 
'crm_category', -- tablename
'N'::bpchar, '', 'D'::character varying, NULL, NULL, 0, 'N'::bpchar, 'Y'::bpchar, 'N'::bpchar, '', 'N'::bpchar, 'L'::bpchar, NULL, '', 'Y'::bpchar, 
NULL::character varying, NULL::bpchar, NULL::bpchar, NULL::bpchar
FROM adempiere.ad_table
;
CREATE TEMP TABLE temp_columns(id int not null, name VARCHAR NOT NULL);
INSERT INTO temp_columns (id, name) values (1, 'AD_Client_ID');
INSERT INTO temp_columns (id, name) values (2, 'AD_Org_ID');
--INSERT INTO temp_columns (name) values ('category_uu');
INSERT INTO temp_columns (id, name) values (4, 'Created');
INSERT INTO temp_columns (id, name) values (5, 'CreatedBy');
INSERT INTO temp_columns (id, name) values (6, 'IsActive');
INSERT INTO temp_columns (id, name) values (7, 'Name');
INSERT INTO temp_columns (id, name) values (8, 'Updated');
INSERT INTO temp_columns (id, name) values (9, 'UpdatedBy');
INSERT INTO temp_columns (id, name) values (10, 'Value');

INSERT INTO adempiere.ad_column(
	ad_column_id, ad_client_id, ad_org_id, isactive, created, updated, createdby, updatedby, name, description, help, version, entitytype, columnname, ad_table_id, ad_reference_id, ad_reference_value_id, ad_val_rule_id, fieldlength, defaultvalue, iskey, isparent, ismandatory, isupdateable, readonlylogic, isidentifier, seqno, istranslated, isencrypted, callout, vformat, valuemin, valuemax, isselectioncolumn, ad_element_id, ad_process_id, issyncdatabase, isalwaysupdateable, columnsql, mandatorylogic, infofactoryclass, isautocomplete, isallowlogging, formatpattern, isallowcopy, seqnoselection, istoolbarbutton, issecure, ad_chart_id, fkconstraintname, fkconstrainttype, pa_dashboardcontent_id)
select (select max(ad_column_id) from adempiere.ad_column)+temp_columns.id, ad_client_id, ad_org_id, isactive, created, updated, createdby, updatedby, 
temp_columns.name, 
description, help, version, entitytype, 
temp_columns.name, 
(select ad_table_id from adempiere.ad_table where name = 'crm_category'), ad_reference_id, ad_reference_value_id, ad_val_rule_id, fieldlength, defaultvalue, iskey, isparent, ismandatory, isupdateable, readonlylogic, isidentifier, seqno, istranslated, isencrypted, callout, vformat, valuemin, valuemax, isselectioncolumn, ad_element_id, ad_process_id, issyncdatabase, isalwaysupdateable, columnsql, mandatorylogic, infofactoryclass, isautocomplete, isallowlogging, formatpattern, isallowcopy, seqnoselection, istoolbarbutton, issecure, ad_chart_id, fkconstraintname, fkconstrainttype, pa_dashboardcontent_id
from adempiere.ad_column inner join temp_columns on temp_columns.name = adempiere.ad_column.columnname
where ad_table_id = (select ad_table_id from adempiere.ad_table where tablename = 'C_BPartner');
INSERT INTO adempiere.ad_table
(ad_table_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, 
help, tablename, isview, accesslevel, entitytype, ad_window_id, ad_val_rule_id, loadseq, issecurityenabled, 
isdeleteable, ishighvolume, importtable, ischangelog, replicationtype, po_window_id, copycolumnsfromtable, iscentrallymaintained, ad_table_uu, processing, 
databaseviewdrop, copycomponentsfromview)
SELECT MAX(ad_table_id)+1, 0, 0, 'Y'::bpchar, now(), 0, now(), 0, 
'crm_customer_category', -- name
'CRM BPartner in Categoríes',  -- description
'', 
'crm_customer_category', -- tablename
'N'::bpchar, '', 'D'::character varying, NULL, NULL, 0, 'N'::bpchar, 'Y'::bpchar, 'N'::bpchar, '', 'N'::bpchar, 'L'::bpchar, NULL, '', 'Y'::bpchar, 
NULL::character varying, NULL::bpchar, NULL::bpchar, NULL::bpchar
FROM adempiere.ad_table
;
INSERT INTO adempiere.ad_column(
	ad_column_id, ad_client_id, ad_org_id, isactive, created, updated, createdby, updatedby, name, description, help, version, entitytype, columnname, ad_table_id, ad_reference_id, ad_reference_value_id, ad_val_rule_id, fieldlength, defaultvalue, iskey, isparent, ismandatory, isupdateable, readonlylogic, isidentifier, seqno, istranslated, isencrypted, callout, vformat, valuemin, valuemax, isselectioncolumn, ad_element_id, ad_process_id, issyncdatabase, isalwaysupdateable, columnsql, mandatorylogic, infofactoryclass, isautocomplete, isallowlogging, formatpattern, isallowcopy, seqnoselection, istoolbarbutton, issecure, ad_chart_id, fkconstraintname, fkconstrainttype, pa_dashboardcontent_id)
select (select max(ad_column_id)+1 from adempiere.ad_column), ad_client_id, ad_org_id, isactive, created, updated, createdby, updatedby, 
'C_BPartner_ID', 
description, help, version, entitytype, 
'C_BPartner_ID', 
(select ad_table_id from adempiere.ad_table where name = 'crm_customer_category'), ad_reference_id, ad_reference_value_id, ad_val_rule_id, fieldlength, defaultvalue, iskey, isparent, ismandatory, isupdateable, readonlylogic, isidentifier, seqno, istranslated, isencrypted, callout, vformat, valuemin, valuemax, isselectioncolumn, ad_element_id, ad_process_id, issyncdatabase, isalwaysupdateable, columnsql, mandatorylogic, infofactoryclass, isautocomplete, isallowlogging, formatpattern, isallowcopy, seqnoselection, istoolbarbutton, issecure, ad_chart_id, fkconstraintname, fkconstrainttype, pa_dashboardcontent_id
from adempiere.ad_column where ad_column_id = (select ad_column_id from ad_column where columnname = 'C_BPartner_ID' order by 1 limit 1);
INSERT INTO adempiere.ad_column(
	ad_column_id, ad_client_id, ad_org_id, isactive, created, updated, createdby, updatedby, name, description, help, version, entitytype, columnname, ad_table_id, ad_reference_id, ad_reference_value_id, ad_val_rule_id, fieldlength, defaultvalue, iskey, isparent, ismandatory, isupdateable, readonlylogic, isidentifier, seqno, istranslated, isencrypted, callout, vformat, valuemin, valuemax, isselectioncolumn, ad_element_id, ad_process_id, issyncdatabase, isalwaysupdateable, columnsql, mandatorylogic, infofactoryclass, isautocomplete, isallowlogging, formatpattern, isallowcopy, seqnoselection, istoolbarbutton, issecure, ad_chart_id, fkconstraintname, fkconstrainttype, pa_dashboardcontent_id)
select (select max(ad_column_id)+1 from adempiere.ad_column), ad_client_id, ad_org_id, isactive, created, updated, createdby, updatedby, 
'category_id', 
description, help, version, entitytype, 
'category_id', 
(select ad_table_id from adempiere.ad_table where name = 'crm_customer_category'), ad_reference_id, ad_reference_value_id, ad_val_rule_id, fieldlength, defaultvalue, iskey, isparent, ismandatory, isupdateable, readonlylogic, isidentifier, seqno, istranslated, isencrypted, callout, vformat, valuemin, valuemax, isselectioncolumn, ad_element_id, ad_process_id, issyncdatabase, isalwaysupdateable, columnsql, mandatorylogic, infofactoryclass, isautocomplete, isallowlogging, formatpattern, isallowcopy, seqnoselection, istoolbarbutton, issecure, ad_chart_id, fkconstraintname, fkconstrainttype, pa_dashboardcontent_id
from adempiere.ad_column where ad_column_id = (select ad_column_id from ad_column where columnname = 'C_BPartner_ID' order by 1 limit 1);
