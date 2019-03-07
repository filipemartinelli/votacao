# App Votacao

## postgree
```
sudo docker run -p 5432:5432 --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=postgres -d postgres:11.1-alpine
```


## rabbit
```
sudo docker run -d --hostname localhost --name rabbitmq -p 5672:5672 -p 5671:5671 -p 15672:15672 rabbitmq:3.6.6-management
```
### rabbit queues
```
docker exec rabbitmq su rabbitmq -- /usr/lib/rabbitmq/bin/rabbitmqctl list_queues
```


## build
```
./gradlew build
```

## test
```
./gradlew test
```


## run
```
./gradlew bootRun
```

## swagger
```
http://localhost:8080/swagger-ui.html#/
```

