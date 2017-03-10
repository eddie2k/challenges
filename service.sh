#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# Keep the pwd in mind!

printusage(){
    US="Usage: $0 {start|stop|block} DATA_FILE [OFF|FATAL|ERROR|WARN|INFO|DEBUG|TRACE|ALL]"
    US1="\tstart <file> [log level]"
    US1_1="\t\tStarts the service and loads the data from the <file>. Log level can be manually defined, but default is INFO."
    US2="\tstop"
    US2_1="\t\tStops the server."
    US3="\tblock <file> [log level]"
    US3_1="\t\tStarts the service, loads the data from the <file> and waits until the service finishes. Log level can be manually defined, but default is INFO."

    USAGE="$US \n$US1 \n$US1_1 \n$US2 \n$US2_1 \n$US3 \n$US3_1"

   echo -e "$USAGE"
}


JAR_FILE="$DIR/target/bus-routes-challenge-service-1.0-jar-with-dependencies.jar"
NAME="bus_routes_service" #no withespaces please! (used for logging purposes; see /tmp)

MAX_HEAP_VALUE=16384m
DATA_FILE=$2
LOG_ARG="logging.threshold"
PIDFILE=/tmp/$NAME.pid
LOGFILE=/tmp/$NAME.log

if [ -z "$3" ]; then
    LOG_LEVEL="INFO"
else
    if [[ ${3^^} =~ ^(OFF|FATAL|ERROR|WARN|INFO|DEBUG|TRACE|ALL)$ ]]; then
        LOG_LEVEL=${3^^}
    else
        LOG_LEVEL=-1
    fi
fi

start() {
    if [ -f $PIDFILE ]; then
        if kill -0 $(cat $PIDFILE); then
            echo 'Service already running' >&2
            return 1
        else
            rm -f $PIDFILE
        fi
    fi
    local CMD="java -D$LOG_ARG=$LOG_LEVEL -Xmx$MAX_HEAP_VALUE -jar $JAR_FILE $DATA_FILE"
    $CMD &>$LOGFILE & echo $! > $PIDFILE
}

stop() {
    if [ ! -f $PIDFILE ] || ! kill -0 $(cat $PIDFILE); then
        echo 'Service not running' >&2
        return 1
    fi
    kill -15 $(cat $PIDFILE) && rm -f $PIDFILE
}


case $1 in
    start)
        if [ $LOG_LEVEL == -1 ]; then
            printusage
        else
            start
        fi
        ;;
    stop)
        if [ -n "$2" ]; then
           printusage 
        else
            stop
        fi;
        ;;
    block)
        if [ $LOG_LEVEL == -1 ]; then
            printusage
        else
            start
        sleep infinity
        fi
        ;;
    *)
        printusage
esac
