# Availabilator

## Introduction
Availabilator is a website monitoring system that is able to collect information about availability of selected locations at a fixed rate in a Postgres database. 
It is composed by 2 pieces that comunicate using Apache Kafka

## The problem
We need to implement a system that:
  - monitors website availability over the network, 
  - produces metrics about this

### Constraints
The system should passes these events through an Aiven Kafka instance into an Aiven PostgreSQL database.
For practical reasons, these components may run in the same machine but in production use similar components would run in different systems.

## The solution

### Website-checker
The website-checker should perform the checks periodically and collect 
  - the HTTP response time, 
  - status code returned,
  - as well as optionally checking the returned page contents for a regexp pattern that is expected to be found on the page.
and sends the check results to a Kafka topic

### DB-writer
For the database writer we expect to see a solution that 
- records the check results into one or more database tables 
- and could handle a reasonable amount of checks performed over a longer period of time.


## Usage

### How to build
The project uses Apache Maven to build so a simple
```
mvn clean install
```
would compile the project

### How to run

#### Java
The project has been configured to create a fatjar after a succesful build in
```
target/web-availability-checker.jar
```
so if you want to run it with default values you just need to
```
java -jar $PATH_TO_TARGET_FOLDER/web-availability-checker.jar
```
will start watching the default location (https://aiven.io)
If you want to watch a different location you can pass it as first argument to the command
```
java -jar $PATH_TO_TARGET_FOLDER/web-availability-checker.jar https://kafka.apache.org/
```

#### Docker
There has been also provided a simple Dockerfile in case you want to run the project in a container.
You'll need to create the image (please notice the `.` at the end)
```
docker build -t web-availability-checker .
```
and then run it
```
docker run -it --rm --network host  web-availability-checker
```
will start watching the default location (https://aiven.io)
If you want to watch a different location you can pass it as argument to the docker run command
```
docker run -it --rm --network host  web-availability-checker https://kafka.apache.org/
```

#### Configuration params
You can tweak the behavior of the system using environment variables
* "availabilator.rate" DEFAULT RATE = 5000
* "availabilator.threadPoolSize" DEFAULT_POOL_SIZE = 2
* "availabilator.initialDelay" DEFAULT_INITIAL_DELAY = 0
* "availabilator.kafka.host" DEFAULT_KAFKA_HOST = "localhost"
* "availabilator.kafka.port" DEFAULT_KAFKA_PORT = 9092

so, for example,
```
docker run -it --rm --network host -e availabilator.rate=60000  web-availability-checker https://www.google.com
```
will start watching https://www.google.com every 60 secs


### AIVEN

Download:
- Access Key
- Access Certificate
- CA Certificate
from Aiven console 


```
docker run -it --rm --network host --env-file .env  web-availability-checker
```








