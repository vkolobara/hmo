//
// Created by vkolobara on 12/24/17.
//

#include <fstream>
#include <sstream>
#include "Parser.h"

using namespace std;

void Parser::parse(string filePath) {
    ifstream file(filePath);

    string line;

    getline(file, line);

    stringstream iss(line);

    double maxWalk;
    unsigned int capacity;
    unsigned int nStops;
    unsigned int nStudents;
    string garbage;

    // Read numbers
    iss >> nStops >> garbage >> nStudents >> garbage >> maxWalk >> garbage >> garbage >> capacity >> garbage;
    this->maxWalk = maxWalk;
    busCapacity = capacity;

    // Skip empty line
    getline(file, line);

    // Read school
    double x,y;
    int id;

    getline(file, line);
    iss.clear();
    iss.str(line);

    iss >> id >> x >> y;
    school = make_shared<BusStop>(id, Coordinate(x,y));

    nStops--;
    while(nStops--) {
        getline(file, line);
        stringstream ss(line);

        iss >> id >> x >> y;
        busStops.emplace_back(id, Coordinate(x,y));
    }

    getline(file, line);
    getline(file, line);

    while(nStudents--) {
        getline(file, line);
        stringstream ss(line);
        iss >> id >> x >> y;
        students.emplace_back(id, Coordinate(x,y));
    }

}

Parser::Parser() {

}

const shared_ptr<BusStop> &Parser::getSchool() const {
    return school;
}

const vector<BusStop> &Parser::getBusStops() const {
    return busStops;
}

const vector<Student> &Parser::getStudents() const {
    return students;
}

unsigned int Parser::getBusCapacity() const {
    return busCapacity;
}

double Parser::getMaxWalk() const {
    return maxWalk;
}
