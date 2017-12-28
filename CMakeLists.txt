cmake_minimum_required(VERSION 3.8)
project(hmo)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES src/main.cpp src/model/Coordinate.cpp src/model/Coordinate.h src/model/Parser.cpp src/model/Parser.h src/algorithm/StudentAssignment.cpp src/algorithm/StudentAssignment.h src/algorithm/Solution.cpp src/algorithm/Solution.h src/algorithm/operators/Mutation.cpp src/algorithm/Algorithm.cpp src/algorithm/operators/Crossover.cpp src/algorithm/operators/Mutation.h src/algorithm/Random.h src/algorithm/Population.cpp src/algorithm/operators/Selection.cpp)

add_executable(hmo ${SOURCE_FILES})