#!/usr/bin/env bash

# Generate a database schema
./gradlew generateReleaseDatabaseSchema
# If there is a [git diff] output, fail the build
if [[ $(git diff --stat) != '' ]]; then
  echo "\033[1;31m> \033[0m"
  echo "\033[1;31mError: Database schema changed without adding a migration. \033[0m"
  echo "\033[1;31m> \033[0m"
  exit 1
else
# Otherwise verify the database migration files
  ./gradlew verifySqlDelightMigration
fi