# Kafka Stream DBWRiter

## Overview

This service listens to JSON messages from Kafka, processes them, and persists them into a database.

#### Technologies Used:
- Java - Programming language (Version 17)
- Spring Boot - Framework for building Java applications
- Kafka - Distributed event streaming platform
- MySQL - Relational database management system
- Docker Compose - Tool for defining and running multi-container Docker application

## Project Structure
```kafka-consumer/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── kafkastreamdbwriter/
│                       ├── config/
│                       │   └── KafkaConsumerConfig.java
│                       ├── model/
│                       │   └── Message.java
│                       ├── repository/
│                       │   └── MessageRepository.java
│                       ├── service/
│                       │   └── KafkaConsumerService.java
│                       └── KafkaStreamDBWriter.java
├── docker-compose.yml
├── application.yml
└── pom.xml


## Pre-Requisites to run this example locally

- install docker-compose [https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)
- modify the ```KAFKA_ADVERTISED_HOST_NAME``` in ```docker-compose.yml``` to match your docker host IP (Note: Do not use localhost or 127.0.0.1 as the host ip if you want to run multiple brokers.)
- if you want to customize any Kafka parameters, simply add them as environment variables in ```docker-compose.yml```, e.g. in order to increase the ```message.max.bytes``` parameter set the environment to ```KAFKA_MESSAGE_MAX_BYTES: 2000000```. To turn off automatic topic creation set ```KAFKA_AUTO_CREATE_TOPICS_ENABLE: 'false'```

## Usage this example locally

Start a cluster:

- ```docker-compose up -d ```

Add more brokers:

- ```docker-compose scale kafka=3```

Destroy a cluster:

- ```docker-compose stop```



