#!/bin/sh

# Define some constants
SLEEPYSIM=com/sleepysim javac -d bin -sourcepath src -cp lib/bcprov-jdk15on-157.jar src/com/sleepysim/Main.java
PROJECT_PATH=$PWD
JAR_PATH=$PROJECT_PATH/lib
BIN_PATH=$PROJECT_PATH/bin
SRC_PATH=$PROJECT_PATH/src/$SLEEPYSIM

# First remove the sources.list file if it exists and then create the sources file of the PROJECT_PATH
rm -f $SRC_PATH/sources
find $SRC_PATH -name *.java > $SRC_PATH/sources.list

# First remove the sleepysim directory if it exists and then create the bin directory of sleepysim
rm -rf $BIN_PATH/$SLEEPYSIM
mkdir $BIN_PATH/$SLEEPYSIM

# Compile the project
javac -d $BIN_PATH/$SLEEPYSIM -classpath $JAR_PATH/bcprov-jdk15on-157.jar  @$SRC_PATH/sources.list
