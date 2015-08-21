#!/bin/bash

# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements. See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership. The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License. You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.

. `dirname $0`/setenv.sh
cd $DATACAT_SERVER_HOME

IS_DAEMON_MODE=false
DATACAT_SERVER_COMMAND=""
STOP=false
FORCE=false

for var in "$@"
do
    case $var in
	start)
	    IS_DAEMON_MODE=true
            shift
        ;;
	stop)
	    STOP=true
	    DATACAT_SERVER_COMMAND="$
	    DATACAT_SERVER_COMMAND $var"
            shift
        ;;
        -h)
            echo "Usage: datacat-server.sh [command-options]"
            echo "command options:"
	    echo "  start              Start server in daemon mode"
	    echo "  stop               Stop server."
	    echo "  -h                 Display this help and exit"
	        shift
            exit 0
        ;;
	*)
	    DATACAT_SERVER_COMMAND="$DATACAT_SERVER_COMMAND $var"
            shift
    esac
done

if $STOP;
then
	for f in `find . -name "*-start_*"`; do
		IFS='_' read -a f_split <<< "$f"
		echo "Found process file : $f"
		echo -n "    Sending kill signals to process ${f_split[1]}..."
		out=`kill -9 ${f_split[1]} 2>&1`
		if [ -z "$out" ]; then
		    echo "done"
		else
		    echo "failed (REASON: $out)"
		fi
		echo -n "    Removing process file..."
		out=`rm $f 2>&1`
		if [ -z "$out" ]; then
		    echo "done"
		else
		    echo "failed (REASON: $out)"
		fi
	done
else
	if $IS_DAEMON_MODE ; then
	    echo "Starting Solr Server in daemon mode..."
	    cd "$DATACAT_SERVER_HOME"/solr-server
		nohup $JAVA_HOME/bin/java  -jar start.jar > solr-server.out & echo $! > "solr-start_$!"
        cd ..
		echo "Starting DataCat server in daemon mode..."
		cd "$DATACAT_SERVER_HOME"/lib
		nohup $JAVA_HOME/bin/java -jar "$DATACAT_SERVER_HOME"/lib/server-1.0-SNAPSHOT.jar > ../datacat-server.out & echo $! > "../server-start_$!"
		cd ..
	else
	    cd "$DATACAT_SERVER_HOME"/solr-server
		nohup $JAVA_HOME/bin/java  -jar start.jar > solr-server.out & echo $! > "solr-start_$!"
        cd ..
        cd "$DATACAT_SERVER_HOME"/lib
		$JAVA_HOME/bin/java -jar "$DATACAT_SERVER_HOME"/lib/server-1.0-SNAPSHOT.jar & echo $! > "server-start_$!"
		cd ..
	fi
fi