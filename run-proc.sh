#!/bin/bash
export LANG="en_US.UTF-8"
java -Xmx800m -Xms800m -Xmn600m -Xss256k -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/dat/log/ibd-scheduler/dump -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/dat/log/ibd-scheduler/gc.log -jar "/opt/java/ibd-scheduler.jar" "--spring.profiles.active=proc" "--ibd={\"logger\": \"${SCHEDULER_LOG}\", \"db\":\"${SCHEDULER_DB}\", \"dbConn\":\"${SCHEDULER_DB_CONN}\"}"
