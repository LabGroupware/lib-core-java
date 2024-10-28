DROP TABLE IF EXISTS core.message CASCADE;
DROP TABLE IF EXISTS core.received_messages CASCADE;

CREATE TABLE core.message (
  id VARCHAR(1000) PRIMARY KEY,
  destination TEXT NOT NULL,
  headers TEXT NOT NULL,
  payload TEXT NOT NULL,
  published SMALLINT DEFAULT 0,
  message_partition SMALLINT,
  creation_time BIGINT
);

CREATE INDEX message_published_idx ON core.message(published, id);

CREATE TABLE core.received_messages (
  consumer_id VARCHAR(1000),
  message_id VARCHAR(1000),
  creation_time BIGINT,
  published SMALLINT DEFAULT 0,
  PRIMARY KEY(consumer_id, message_id)
);

CREATE TABLE core.offset_store(
  client_name VARCHAR(255) NOT NULL PRIMARY KEY,
  serialized_offset VARCHAR(255)
);