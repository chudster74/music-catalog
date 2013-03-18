#!/bin/bash
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ---------------------------------------------------------------------------
# Start script for the etl Server
#
# $Id: startup.bat 562770 2007-08-04 22:13:58Z markt $
# ---------------------------------------------------------------------------
# Guess APP_HOME if not defined
CURRENT_DIR=`pwd`
if [ "$APP_HOME" == "" ]
then
APP_HOME=$CURRENT_DIR
fi

if [ ! -f "$APP_HOME/bin/${project.name}-${project.version}.jar" ]
then
echo -e "The APP_HOME environment variable is not defined correctly"
echo -e "This environment variable is needed to run this program"
exit -1
fi

EXECUTABLE="$APP_HOME/bin/${project.name}-${project.version}.jar"

# Get remaining unshifted command line arguments and save them in the
CMD_LINE_ARGS=$*

cd "$APP_HOME/bin"
java -Dfile.encoding=UTF-8 -XX:NewSize=256m -XX:MaxNewSize=256m -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -Xms512m -Xmx512m -jar "$EXECUTABLE" start $CMD_LINE_ARGS

