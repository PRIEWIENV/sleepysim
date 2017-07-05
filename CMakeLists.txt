cmake_minimum_required(VERSION 3.7)
project(sleepysim)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES src/main.cpp src/Signature.cpp src/Signature.h src/header.h src/Message.cpp src/Message.h src/Node.cpp src/Node.h src/Framework.cpp src/Framework.h)
add_executable(sleepysim ${SOURCE_FILES})