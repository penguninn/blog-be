#!/bin/sh
set -eu

keycloak_database="${KEYCLOAK_DATABASE:-keycloak}"

psql --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
  --set=ON_ERROR_STOP=1 --set=keycloak_database="$keycloak_database" <<-'EOSQL'
SELECT 'CREATE DATABASE ' || quote_ident(:'keycloak_database')
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = :'keycloak_database'
)\gexec
EOSQL
