CREATE SCHEMA core;

DROP TABLE IF EXISTS core.events CASCADE;
DROP TABLE IF EXISTS core.entities CASCADE;
DROP TABLE IF EXISTS core.snapshots CASCADE;
DROP TABLE IF EXISTS core.cdc_monitoring CASCADE;

CREATE TABLE core.events (
  event_id VARCHAR(1000) PRIMARY KEY,
  event_type VARCHAR(1000),
  event_data VARCHAR(1000) NOT NULL,
  entity_type VARCHAR(1000) NOT NULL,
  entity_id VARCHAR(1000) NOT NULL,
  triggering_event VARCHAR(1000),
  metadata VARCHAR(1000),
  published SMALLINT DEFAULT 0
);

CREATE INDEX events_idx ON core.events(entity_type, entity_id, event_id);
CREATE INDEX events_published_idx ON core.events(published, event_id);

CREATE TABLE core.entities (
  entity_type VARCHAR(1000),
  entity_id VARCHAR(1000),
  entity_version VARCHAR(1000) NOT NULL,
  PRIMARY KEY(entity_type, entity_id)
);

CREATE INDEX entities_idx ON core.entities(entity_type, entity_id);

CREATE TABLE core.snapshots (
  entity_type VARCHAR(1000),
  entity_id VARCHAR(1000),
  entity_version VARCHAR(1000),
  snapshot_type VARCHAR(1000) NOT NULL,
  snapshot_json VARCHAR(1000) NOT NULL,
  triggering_events VARCHAR(1000),
  PRIMARY KEY(entity_type, entity_id, entity_version)
);

CREATE TABLE core.cdc_monitoring (
  reader_id VARCHAR(1000) PRIMARY KEY,
  last_time BIGINT
);