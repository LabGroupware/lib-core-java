## ECR Push

### Postgres
```sh
docker build -t ablankz/postgres:1.2.0 postgres
./ecr-push.sh postgres 1.2.0 ablankz/postgres:1.2.0
```

### Debezium
```sh
docker build -f debezium/postgres/Dockerfile  -t ablankz/debezium:1.2.0 .
./ecr-push.sh debezium 1.2.0 ablankz/debezium:1.2.0
```
