# Kafka Consumer

## Objective
We need a Kafka consumer storing the data (from a Kafka topic) to an Aiven PostgreSQL database.
For the database writer we expect to see a solution that
- records the check results into one or more database tables
- and could handle a reasonable amount of checks performed over a longer period of time.

## Usage

### Configuration
This system can be configured using environment variables.
This is the list of accepted properties:
- BOOTSTRAP_SERVERS: Kafka URL
- TOPIC: Topic name to consume
- GROUP_ID: Kafka consumer group id
- AUTO_COMMIT_INTERVAL_MS: Interval to commit consumed offset to Kafka in milliseconds 
- POLLING_TIMEOUT: Timeout when polling Kafka 
- SSL_ENABLED: true/false to indicate if we are using SSL to connect to Kafka
- TRUSTSTORE: Truststore location
- TRUSTSTORE_PASS: Truststore password
- KEYSTORE: Keystore location containing 
- KEYSTORE_PASS: Keystore password
- KEYSTORE_TYPE: PKCS12
- KEY_PASS: Key password in configured Keystore
- DB_HOST: Host where POSTGRES is located
- DB_PORT: Port to connect to POSTGRES
- DB_USER: Username to connect to POSTGRES
- DB_NAME: Name of the database in POSTGRES
- DB_USER_PASS: User password to connect to POSTGRES 
- DB_TABLE_NAME: Name of the table where data is going to be stored.

### Provisioning the infrastructure
The process will process data from an existing Kafka topic containing Availability records with this shape:

```json
{
    "schema": {
        "type": "struct",
        "fields": [
            { "field": "timestamp", "type": "int64", "name": "org.apache.kafka.connect.data.Timestamp", "optional": false },
            { "field": "address", "type": "string", "optional": false },
            { "field": "responseTime", "type": "int32", "optional": false },
            { "field": "statusCode", "type": "int16", "optional": false },
            { "field": "matches", "type": "boolean", "optional": true }
        ]
    },
    "payload": {
      "timestamp":1620074798667,
      "address":"https://aiven.io",
      "responseTime":288,
      "statusCode":200
    }
}
```

and it will store them in a table in a PG database. There is a SQL script (src/main/resources/db.sql) to create the DB and the table.


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
$ ~/labs/availabilator/consumer > openssl pkcs12 -export -inkey service.key -in service.cert -out client.keystore.p12 -name service_key
```
to create the keystore and
```
$ ~/labs/availabilator/consumer > keytool -import -file ca.pem -alias CA -keystore client.truststore.jks 
```
to create the truststore.


### How to build
The project uses Apache Maven as build tool so a simple command
```
$ ~/labs/availabilator/consumer > mvn clean install
```
would compile the project


### How to run

#### Java
The project has been configured to create a fatjar after a succesful build in
```
target/db-writer.jar
```
so you can launch it with default values running
```
java -jar $PATH_TO_TARGET_FOLDER/db-writer.jar
```
and it will start consuming the data from Kafka using default values. You can use environment variables to configure the service.


### Docker
There has been also provided a simple Dockerfile in case you want to run the project in a container.
You'll need to create the image (please notice the `.` at the end)
```
docker build -t db-writer .
```
and then run it
```
docker run -it --rm db-writer
```

You can group all your environment variables in an .env file and pass it to docker engine when run the image.
```
docker run -it --rm --env-file .env  db-writer
```
