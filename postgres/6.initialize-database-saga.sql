DROP Table IF Exists core.saga_instance_participants;
DROP Table IF Exists core.saga_instance;
DROP Table IF Exists core.saga_lock_table;
DROP Table IF Exists core.saga_stash_table;

CREATE TABLE core.saga_instance_participants (
  saga_type VARCHAR(512) NOT NULL,
  saga_id VARCHAR(100) NOT NULL,
  destination VARCHAR(512) NOT NULL,
  resource VARCHAR(512) NOT NULL,
  PRIMARY KEY(saga_type, saga_id, destination, resource)
);

CREATE TABLE core.saga_instance(
  saga_type VARCHAR(512) NOT NULL,
  saga_id VARCHAR(100) NOT NULL,
  state_name VARCHAR(100) NOT NULL,
  last_request_id VARCHAR(100),
  end_state BOOLEAN,
  compensating BOOLEAN,
  failed BOOLEAN,
  saga_data_type VARCHAR(1024) NOT NULL,
  saga_data_json TEXT NOT NULL,
  PRIMARY KEY(saga_type, saga_id)
);

create table core.saga_lock_table(
  target VARCHAR(512) PRIMARY KEY,
  saga_type VARCHAR(512) NOT NULL,
  saga_Id VARCHAR(100) NOT NULL
);

create table core.saga_stash_table(
  message_id VARCHAR(100) PRIMARY KEY,
  target VARCHAR(512) NOT NULL,
  saga_type VARCHAR(512) NOT NULL,
  saga_id VARCHAR(100) NOT NULL,
  message_headers TEXT NOT NULL,
  message_payload TEXT NOT NULL
  );
