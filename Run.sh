#!/bin/sh

# Define some constants
SLEEPYSIM=com/sleepysim
PROJECT_PATH=$PWD
JAR_PATH=$PROJECT_PATH/lib
BIN_PATH=$PROJECT_PATH/bin

# Run the project as a background process
java -classpath $BIN_PATH:$JAR_PATH/bcprov-jdk15on-157.jar com.sleepysim.Main 0
