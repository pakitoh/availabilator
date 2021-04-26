resource "aiven_kafka" "my-kafka" {
    project = data.aiven_project.my-project.project
    cloud_name = "google-europe-north1"
    plan = "business-4"
    service_name = "my-kafka"
    maintenance_window_dow = "sunday"
    maintenance_window_time = "10:00:00"

    kafka_user_config {
        kafka_rest = true
        kafka_connect = true
        kafka_version = "2.7"

        kafka {
            group_max_session_timeout_ms = 70000
            log_retention_bytes = 1000000000
            auto_create_topics_enable = true
        }

        public_access {
            kafka = true
            kafka_rest = true
            kafka_connect = true
        }
    }
}

resource "aiven_kafka_topic" "my-topic" {
    project = data.aiven_project.my-project.project
    service_name = aiven_kafka.my-kafka.service_name 
    topic_name = var.topic_name
    partitions = 5
    replication = 3

    config {
        flush_ms = 10
        unclean_leader_election_enable = true
        cleanup_policy = "compact,delete"
    }

    timeouts {
        create = "5m"
        read = "5m"
    }
}

resource "aiven_kafka_connector" "my-kafka-pg-connector" {
  project = data.aiven_project.my-project.project
  service_name = aiven_kafka.my-kafka.service_name
  connector_name = "kafka-pg-sink"

  config = {
    "name" = "kafka-pg-sink"
    "topics" = aiven_kafka_topic.my-topic.topic_name
    "connector.class" = "io.aiven.connect.jdbc.JdbcSinkConnector"
    "key.converter" = "org.apache.kafka.connect.storage.StringConverter"
    "value.converter" = "org.apache.kafka.connect.json.JsonConverter"
    "key.converter.schemas.enable" = "true"
    "value.converter.schemas.enable" = "true"
    "insert.mode" = "insert"
    "pk.mode" = "record_value"
    "pk.fields" = "timestamp"
    "auto.create" = "true"
    "connection.url" = "jdbc:postgresql://${aiven_pg.pg.service_host}:${aiven_pg.pg.service_port}/${postgresql_database.my-db.name}?user=${aiven_pg.pg.service_username}&password=${aiven_pg.pg.service_password}"
  }

 depends_on = [
    aiven_kafka.my-kafka,
    postgresql_database.my-db
  ]
}



