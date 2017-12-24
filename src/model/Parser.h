//
// Created by vkolobara on 12/24/17.
//

#ifndef HMO_PARSER_H
#define HMO_PARSER_H


#include <vector>
#include <string>
#include <memory>
#include "BusStop.h"
#include "Student.h"

class Parser {
private:
    std::shared_ptr<BusStop> school;
    std::vector<BusStop> busStops;
    std::vector<Student> students;
    unsigned int busCapacity;
    double maxWalk;
public:
    Parser();
    void parse(std::string file);

    const std::shared_ptr<BusStop> &getSchool() const;

    const std::vector<BusStop> &getBusStops() const;

    const std::vector<Student> &getStudents() const;

    unsigned int getBusCapacity() const;

    double getMaxWalk() const;
};


#endif //HMO_PARSER_H
