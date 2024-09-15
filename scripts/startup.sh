#!/bin/bash

set -e -o pipefail

source "`dirname "$0"`"/set_env.sh

export MODE="standalone"

while getopts ":m:" opt
do
    case $opt in
        m)
            MODE=$OPTARG;;
        ?)
        echo "Unknown parameter"
        exit 1;;
    esac
done

# use a small heap size for the CLI tools, and thus the serial collector to
# avoid stealing many CPU cycles; a user can override by setting CLI_JAVA_OPTS
CLI_JAVA_OPTS="-Xms4m -Xmx64m -XX:+UseSerialGC ${CLI_JAVA_OPTS}"

LAUNCHER_CLASSPATH=$APP_HOME/libs/*

exec \
  "$JAVA" \
  $CLI_JAVA_OPTS \
  -Dlogback.configurationFile="file://${APP_CONF}/logback.xml" \
  -Dlogs.path="${APP_HOME}/logs" \
  -Dspring.config.additional-location="file://${APP_CONF}/application-${MODE}.yaml" \
  -Dapp.home="$APP_HOME" \
  -cp "$LAUNCHER_CLASSPATH" \
  com.honing.sdcc.SdccApplication \
  "$@"
