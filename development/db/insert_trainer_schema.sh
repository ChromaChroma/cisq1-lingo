#!/bin/bash
set -e

echo "Importing trainer schema"

PGPASSWORD=${DB_APP_PASS} psql --username ${DB_APP_USER} ${DB_APP_NAME} --file /data/trainer_schema.sql