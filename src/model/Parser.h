//
// Created by vkolobara on 12/24/17.
//

#ifndef HMO_PARSER_H
#define HMO_PARSER_H


#include <vector>
#include <string>
#include <memory>
#include "Coordinate.h"

class Parser {
private:
    std::shared_ptr<Coordinate> school;
    std::vector<Coordinate> busStops;
    std::vector<Coordinate> students;
    unsigned int busCapacity;
    double maxWalk;
    std::vector<std::vector<double>> studentToStopDistance;
    std::vector<double> stopToSchoolDistance;
public:
    Parser();
    void parse(std::string file);

    const std::vector<std::vector<double>> &getStudentToStopDistance() const;

    virtual ~Parser();

    const std::shared_ptr<Coordinate> &getSchool() const;

    const std::vector<Coordinate> &getBusStops() const;

    const std::vector<double> &getStopToSchoolDistance() const;

    const std::vector<Coordinate> &getStudents() const;

    unsigned int getBusCapacity() const;

    double getMaxWalk() const;
};


#endif //HMO_PARSER_H
