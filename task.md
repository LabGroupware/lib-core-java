# Task

## Docker Image

login
``` sh
docker login
```

build
``` sh
docker build -t ablankz/postgres:1.0.0 postgres
docker build -f debezium/postgres/Dockerfile  -t ablankz/debezium:1.0.0 .
```

push
``` sh
docker push ablankz/postgres:1.0.0
docker push ablankz/debezium:1.0.0
```