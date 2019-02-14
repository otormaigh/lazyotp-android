#!/bin/bash

set -e

# Echoes extra logging info
log() {
  if [ "$VERBOSE" = true ]
    then
      echo "--${1}"
  fi
}

# Checks if an env value is present and not empty
checkEnv() {
  echo "Checking"
  env_value=$(printf '%s\n' "${!1}")
  if [ -z ${env_value} ]; then
    echo "$1 is undefined, exiting..."
    exit 1
  else
    log "Found value for $1"
  fi
}

checkEnv LAZY_SMS_ENCRYPT_KEY

echo "Encrypting files"
openssl aes-256-cbc -a -md sha256 -in signing/release.keystore -out enc/release.keystore.aes -k $LAZY_SMS_ENCRYPT_KEY
openssl aes-256-cbc -a -md sha256 -in enc.properties -out enc/enc.properties.aes -k $LAZY_SMS_ENCRYPT_KEY
log "Files encrypted"

echo "Finishing up"