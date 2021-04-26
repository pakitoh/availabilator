output "db_host" {
  value = aiven_pg.pg.service_host
}

output "dp_port" {
  value = aiven_pg.pg.service_port
}

output "db_name" {
  value = postgresql_database.my-db.name
}

output "kafka_host" {
  value = aiven_kafka.my-kafka.service_host
}

output "kafka_port" {
  value = aiven_kafka.my-kafka.service_port
}

