# Getting Started

Please note that application is build using intellij Idea, Java 8, 
docker version (Docker version 20.10.5, build 55c4c88)
docker-compose version (docker-compose version 1.29.0, build 07737305)
macos bigsur big sur 11.3
## Reference Documentation

## Pre-Requisites

- install docker & docker-compose [https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)

- install java-8 (kafka will run only on java-8)

```
cd kafka-docker directory

Edit docker-compose.yml
1. modify the KAFKA_ADVERTISED_HOST_NAME in [docker-compose.yml] to match your docker host IP (Note: Do not use localhost or 127.0.0.1 as the host ip if you want to run multiple brokers.)
2. Change the docker host IP in KAFKA_ADVERTISED_HOST_NAME
On macOS use the following script to set DOCKERHOST env var
DOCKERHOST=$(ifconfig | grep -E "([0-9]{1,3}\.){3}[0-9]{1,3}" | grep -v 127.0.0.1 | awk '{ print $2 }' | cut -f2 -d: | head -n1)
or use DOCKERHOST ip as KAFKA_ADVERTISED_HOST_NAME 


// Start services based on the default docker-compose.yml
// You may want to use -d to detach from the terminal (daemon mode)

$ docker-compose up 

/* Connect to the container of the Kafka broker
 NOTE: Asciidoc would try to substitute curly braces
        Remove the spaces between curly braces */
        
$ docker ps --format "{ { .Names } }"
#output
kafka-docker_zookeeper_1
kafka-docker_kafka_1

// Print out the topics
// You should see 1 topics listed
$ docker exec -t kafka-docker_kafka_1 \
  kafka-topics.sh \
    --bootstrap-server :9092 \
    --list


```
### PostgresDb Setup

```
navigate to project root directory 
// Start postgress services based on the default root directory docker-compose.yml
// You may want to use -d to detach from the terminal (daemon mode)
$ docker-compose up  

```

* ### All Requirements are implemented mention in the challenge 

## Api Usage

User Api

```
1. As a non-user, I can create my account by providing providing my nick name 

###
POST http://localhost:9000/api/user/register
Content-Type: application/json

{
  "nickname": "test1"
}
```
Message Api 

```
2. As a user, I can send a message to another user identified by his/her nickname/id 

###
POST http://localhost:9000/api/message/
Content-Type: application/json
nick-name: nickname #Header value

#body
{
  "to": "toNickName",
  "message": "test message"
}

3. As a user, I can view all message that i received

###
GET http://localhost:9000/api/message/received
Content-Type: application/json
nick-name: UserNickName #Header

4. As a user, i can view all message that i sent 

###
GET http://localhost:9000/api/message/sent
Content-Type: application/json
nick-name: UserNickName #Header


5. As as user, I can view all messages received from particular user 

###
GET http://localhost:9000/api/message/search/{user}
Content-Type: application/json
nick-name: UserNickName

{user} = nickname/id

```

## Deployment

build docker image from dockerfile, docker file present in project root directory 
```
docker build -t message-app:latest .

#if docker failed to build then use to disable buildkit feature

DOCKER_BUILDKIT=0 docker build -t message-app:latest .
```

