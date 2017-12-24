cmake_minimum_required(VERSION 3.8)
project(hmo)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES src/main.cpp src/algorithm/Solution.cpp src/algorithm/Solution.h src/model/Student.cpp src/model/Student.h src/model/Coordinate.cpp src/model/Coordinate.h src/model/BusStop.cpp src/model/BusStop.h src/model/Parser.cpp src/model/Parser.h src/algorithm/StudentOnBusStop.cpp src/algorithm/StudentOnBusStop.h)

add_executable(hmo ${SOURCE_FILES})