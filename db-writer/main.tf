terraform {
  required_providers {
    aiven = {
      source = "aiven/aiven"
      version = "2.1.11"
    }
    postgresql = {
      source  = "cyrilgdn/postgresql"
      version = "1.12.1"
    }
  }
}

provider "aiven" {
  api_token = var.api_token
}

data "aiven_account" "my-account" {
  name = var.account_name
}

data "aiven_project" "my-project" {
  project = "availabilator"
  account_id = data.aiven_account.my-account.account_id
}
