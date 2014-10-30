#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

export CLASSPATH=${DIR}/../../libs/robocode.jar:${DIR}/..

echo CLASSPATH: ${CLASSPATH}
