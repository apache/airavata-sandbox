#!/bin/sh

for i in target/lib/*.jar
do
  CLASSPATH=$CLASSPATH:$i
done

CLASSPATH=$CLASSPATH:target/GRAM5-Lite-1.0.jar:./config

echo $CLASSPATH

$JAVA_HOME/bin/java -classpath $CLASSPATH org.apache.airavata.jobsubmission.gram.FileTransfer $*
