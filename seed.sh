#!/bin/bash

# Configuration
CONTAINER_NAME="nbd-cassandra1-1"
USER="cassandra"
PASS="cassandrapassword"
SCHEMA_FILE="./schema.cql"

# Check if the file exists before trying to run it
if [ ! -f "$SCHEMA_FILE" ]; then
    echo "Error: $SCHEMA_FILE not found!"
    exit 1
fi

echo "Applying $SCHEMA_FILE to $CONTAINER_NAME..."

# Execute the local file inside the container
docker exec -i $CONTAINER_NAME cqlsh -u $USER -p $PASS < "$SCHEMA_FILE"

if [ $? -eq 0 ]; then
    echo "Success: Schema applied."
else
    echo "Error: Failed to apply schema. Check if the container is running and healthy."
fi