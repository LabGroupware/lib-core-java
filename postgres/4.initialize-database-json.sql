ALTER TABLE core.message DROP COLUMN payload;
ALTER TABLE core.message ADD COLUMN payload JSON;

ALTER TABLE core.message DROP COLUMN headers;
ALTER TABLE core.message ADD COLUMN headers JSON;
