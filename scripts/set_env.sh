#!/bin/bash

set -e -o pipefail

error_exit ()
{
    echo "ERROR: $1 !!"
    exit 1
}


CDPATH=""

SCRIPT="$0"

# SCRIPT might be an arbitrarily deep series of symbolic links; loop until we
# have the concrete path
while [ -h "$SCRIPT" ] ; do
  ls=`ls -ld "$SCRIPT"`
  # Drop everything prior to ->
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    SCRIPT="$link"
  else
    SCRIPT=`dirname "$SCRIPT"`/"$link"
  fi
done

# determine Elasticsearch home; to do this, we strip from the path until we find
# bin, and then strip bin (there is an assumption here that there is no nested
# directory under bin also named bin)
APP_HOME=`dirname "$SCRIPT"`

# now make APP_HOME absolute
APP_HOME=`cd "$APP_HOME"; pwd`

while [ "`basename "$APP_HOME"`" != "bin" ]; do
  APP_HOME=`dirname "$APP_HOME"`
done
APP_HOME=`dirname "$APP_HOME"`

[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
[ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME

if [ -z "$JAVA_HOME" ]; then
  if $darwin; then

    if [ -x '/usr/libexec/java_home' ] ; then
      export JAVA_HOME=`/usr/libexec/java_home`

    elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
      export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
    fi
  else
    JAVA_PATH=`dirname $(readlink -f $(which javac))`
    if [ "x$JAVA_PATH" != "x" ]; then
      export JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
    fi
  fi
  if [ -z "$JAVA_HOME" ]; then
        error_exit "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk17 or later is better!"
  fi
fi

export JAVA=$JAVA_HOME/bin/java
# do not let JAVA_TOOL_OPTIONS slip in (as the JVM does by default)
if [ ! -z "$JAVA_TOOL_OPTIONS" ]; then
  echo "warning: ignoring JAVA_TOOL_OPTIONS=$JAVA_TOOL_OPTIONS"
  unset JAVA_TOOL_OPTIONS
fi


# JAVA_OPTS is not a built-in JVM mechanism but some people think it is so we
# warn them that we are not observing the value of $JAVA_OPTS
if [ ! -z "$JAVA_OPTS" ]; then
  echo -n "warning: ignoring JAVA_OPTS=$JAVA_OPTS; "
  echo "pass JVM parameters via ES_JAVA_OPTS"
fi

export HOSTNAME=$HOSTNAME

if [ -z "$APP_CONF" ]; then APP_CONF="$APP_HOME"/config; fi

if [ -z "$APP_CONF" ]; then
  echo "$APP_CONF must be set to the configuration path"
  exit 1
fi

# now make APP_CONF absolute
APP_CONF=`cd "$APP_CONF"; pwd`


cd "$APP_HOME"
