SELECT * FROM pg_create_logical_replication_slot('core_slot', 'wal2json');
SELECT * FROM pg_create_logical_replication_slot('core_slot2', 'wal2json');