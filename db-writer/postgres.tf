resource "aiven_pg" "pg" {
    project = data.aiven_project.my-project.project
    cloud_name = "google-europe-north1"
    plan = "startup-4"
    service_name = "my-pg"
    maintenance_window_dow = "sunday"
    maintenance_window_time = "10:00:00"

    pg_user_config {
        pg_version = 12
        admin_username = "pgadmin"
        admin_password = var.pg_pass

        public_access {
            pg = true
            prometheus = false
        }

        pg {
            idle_in_transaction_session_timeout = 900
            log_min_duration_statement = -1
        }
    }

    timeouts {
        create = "20m"
        update = "15m"
    }
}

provider "postgresql" {
  host = aiven_pg.pg.service_host
  port = aiven_pg.pg.service_port
  database = "defaultdb"
  username = aiven_pg.pg.service_username
  password = aiven_pg.pg.service_password
  sslmode = "require"
  connect_timeout = 15
}

resource postgresql_database "my-db" {
  name = "my-db"
}

