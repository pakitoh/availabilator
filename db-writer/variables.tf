variable "account_name" {
  description = "Aiven account"
  type        = string
  sensitive   = true
}

variable "api_token" {
  description = "API Token to grant access to Aiven account"
  type        = string
  sensitive   = true
}

variable "user_email" {
  description = "User email"
  type        = string
  sensitive   = true
}

variable "pg_pass" {
  description = "Postgres admin user password"
  type        = string
  sensitive   = true
}

variable "topic_name" {
  description = "Name of the Kafka topic"
  type        = string
  default     = "availability"
}

