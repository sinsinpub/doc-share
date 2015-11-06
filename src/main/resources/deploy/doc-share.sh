#!/usr/bin/env bash
#
# SYS-V init startup script at /etc/init.d/.
# For Linux only, not tested on Unix, cygwin, darwin.
#
# @author sin_sin

NAME=$(echo $(basename $0) | sed -e 's/^[SK][0-9]*//' -e 's/\.sh$//')

### BEGIN INIT INFO
# Provides:          $NAME
# Required-Start:    $local_fs $network
# Required-Stop:     $local_fs $network
# Should-Start:      $named
# Should-Stop:       $named
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start $NAME daemon.
# Description:       Start $NAME daemon.
### END INIT INFO

WORKDIR="$(cd "$(dirname "$0")" && pwd)"
SCRIPTDIR="$(dirname "$(readlink -f "$0")")"
CONFNAME="$SCRIPTDIR/$NAME.conf"

# Source environment config
[ -f "$CONFNAME" ] && source $CONFNAME

# Config defaults
RUN_AS=${USER:-"$(whoami)"}
JAVA_HOME=${JAVA_HOME:-"/usr/java/default"}
JAVA_OPTS=${JAVA_OPTS:-"-server"}
APP_HOME=${APP_HOME:-"$SCRIPTDIR"}
WAR_NAME=${WAR_NAME:-"$NAME.war"}
CONSOLE_LOG=${CONSOLE_LOG:-"$APP_HOME/console.log"}
PORT=${PORT:-"8080"}
CONTEXT_PATH=${CONTEXT_PATH:-"/"}
LANG=${LANG:-"en_US.UTF-8"}
SHOW_INFO=${SHOW_INFO:-""}

# For SELinux we need to use 'runuser' not 'su'
if [ -x "/sbin/runuser" ]; then
    SU="/sbin/runuser -p -s /bin/sh"
else
    SU="/bin/su -p -s /bin/sh"
fi

# Variables
RETVAL=0
JAVACMD="$JAVA_HOME/bin/java $JAVA_OPTS -jar $APP_HOME/$WAR_NAME $PORT $CONTEXT_PATH"
STARTCMD="/usr/bin/nohup $JAVACMD > $CONSOLE_LOG 2>&1 &"

export LANG
export JAVA_HOME
export APP_HOME
cd $APP_HOME

# Functions
d_showinfo() {
    echo "Daemon name       : $NAME"
    echo "Run as            : $RUN_AS"
    echo "Working directory : $WORKDIR"
    echo "Script directory  : $SCRIPTDIR"
    echo "Config name       : $CONFNAME"
    echo "Java Home         : $JAVA_HOME"
    echo "Java Options      : $JAVA_OPTS"
    echo "App Home          : $APP_HOME"
    echo "Console Log       : $CONSOLE_LOG"
#    echo "Command line      : $STARTCMD"
    echo ""
}

d_start() {
    d_pspid
    if [ $? -eq 0 ]; then
        echo "$NAME daemon is still running: $PID"
        RETVAL=2
        return $RETVAL
    fi
    echo "Starting $NAME daemon..."
    $SU $RUN_AS -c "$STARTCMD"
    RETVAL=$?
    sleep 1
    d_status
    return $RETVAL
}

d_stop() {
    d_status
    if [ $? -ne 0 ]; then
        RETVAL=2
        return $RETVAL
    fi
    echo "Stopping $NAME daemon..."
    kill $PID
    RETVAL=$?
    sleep 1
    d_status
    return $RETVAL
}

d_restart() {
    d_stop
    if [ $? -ne 0 ]; then
        RETVAL=$?
        return $RETVAL
    fi
    sleep 1
    d_start
    return $RETVAL
}

d_status() {
    d_pspid
    if [ -z "$PID" ]; then
        echo "$NAME daemon is not running"
        return 1
    fi
    echo "$NAME daemon is running: $PID"
    return 0
}

d_pspid() {
    PID=$(/bin/ps -ef | /bin/awk '/java .*-jar .*'$WAR_NAME'.*$/ && !/awk/ {print$2}')
    if [ -z "$PID" ]; then
        return 1
    fi
    return 0
}

# Switch cases
case $1 in
    start)
        d_start
    ;;
    stop)
        d_stop
    ;;
    restart)
        d_restart
    ;;
    status)
        if [ -z "$SHOW_INFO" ]; then
            d_showinfo
        fi
        d_status
    ;;
    *)
        echo "usage: $NAME {start|stop|restart|status}"
        RETVAL=1
    ;;
esac

exit $RETVAL
