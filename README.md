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

### Provisioning the infrastructure
db-writer folder contains Terraform scripts to create and configure Kafka and Postgres in Aiven cloud. 
You just need to create a file `secrets.tfvars` with needed secrets to connect to Aiven cloud. You can use the `secrets.tfvars.tmp` as a template.
After that you just need to `terraform apply` as usual:
```
$ ~/labs/availabilator > cd db-writer
$ ~/labs/availabilator/db-writer > terraform --version
Terraform v0.15.0
on linux_amd64
$ ~/labs/availabilator/db-writer > terraform init
[...]
$ ~/labs/availabilator/db-writer > terraform apply -var-file="secrets.tfvars"
[...]
```

### Configure SSL to connect Aiven cloud
You will need to download 
- Access Key
- Access Certificate
- CA Certificate
from Kafka service in Aiven console. 
You can do this browsing manually to the web console or using avn cli like this:
```
avn service list my-kafka --json | jq -r '.[0].connection_info.kafka_access_cert' 
```
Download those 3 files ca.pem, service.cert and service.key to checker folder and run:

```
$ ~/labs/availabilator/checker > openssl pkcs12 -export -inkey service.key -in service.cert -out client.keystore.p12 -name service_key
```
to create the keystore and 
```
$ ~/labs/availabilator/checker > keytool -import -file ca.pem -alias CA -keystore client.truststore.jks 
```
to create the truststore.


### How to build
The project uses Apache Maven as build tool so a simple command
```
$ ~/labs/availabilator/checker > mvn clean install
```
would compile the project


### How to run

#### Java
The project has been configured to create a fatjar after a succesful build in
```
target/web-availability-checker.jar
```
so you can launch it with default values running
```
java -jar $PATH_TO_TARGET_FOLDER/web-availability-checker.jar
```
and it will start watching the default location (https://aiven.io)
If you want to watch a different location you can pass it as first argument to the java command
```
java -jar $PATH_TO_TARGET_FOLDER/web-availability-checker.jar https://kafka.apache.org/
```
You can use environment variables to configure the service.

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
You can tweak the behavior of the system using environment variables like here
```
docker run -it --rm --network host -e POOLING_RATE=60000  web-availability-checker https://www.google.com
```
in this example the system will start watching https://www.google.com every 60 secs.
We can also grouping several env vars in an .env file
```
POOLING_RATE=1000
KAFKA_HOST=my-kafka-availabilator.aivencloud.com
KAFKA_PORT=17701
SSL_ENABLED=true
TRUSTSTORE=client.truststore.jks
TRUSTSTORE_PASS=secret
KEYSTORE=client.keystore.p12
KEYSTORE_PASS=secret
KEY_PASS=secret

```
And the using with docker
```
docker run -it --rm --network host --env-file .env  web-availability-checke
```

## TODO
- Add integration and acceptance tests
- Add a CI pipeline
- Install TimescaleDB extension in PG
- Add a REST interface to be able to add at runtime watches on several sites running in the same service.

