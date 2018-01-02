//
// Created by vkolobara on 12/24/17.
//

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

    const unsigned int rows = nStudents;
    const unsigned int cols = nStops;

    // Skip empty line
    getline(file, line);

    // Read school
    double x,y;
    int id;

    getline(file, line);
    iss.clear();
    iss.str(line);

    iss >> id >> x >> y;
    school = make_shared<Coordinate>(Coordinate(x,y));

    nStops--;
    while(nStops--) {
        getline(file, line);
        stringstream ss(line);

        ss >> id >> x >> y;
        Coordinate coord(x,y);
        busStops.emplace_back(coord);
        stopToSchoolDistance.emplace_back(Coordinate::euclideanDistance(*school.get(), coord));
    }

    getline(file, line);
    getline(file, line);

    while(nStudents--) {
        getline(file, line);
        stringstream ss(line);
        ss >> id >> x >> y;
        Coordinate coord(x,y);
        students.emplace_back(coord);

        vector<double> distances;

        for (int i=0; i<busStops.size(); i++) {
            double dst = Coordinate::euclideanDistance(coord, busStops[i]);
            distances.push_back(dst);
        }

        studentToStopDistance.push_back(distances);

    }
    file.close();

}

Parser::Parser() {

}

unsigned int Parser::getBusCapacity() const {
    return busCapacity;
}

double Parser::getMaxWalk() const {
    return maxWalk;
}

const shared_ptr<Coordinate> &Parser::getSchool() const {
    return school;
}

const vector<Coordinate> &Parser::getBusStops() const {
    return busStops;
}

const vector<Coordinate> &Parser::getStudents() const {
    return students;
}

Parser::~Parser() {
}

const vector<vector<double>> &Parser::getStudentToStopDistance() const {
    return studentToStopDistance;
}

const vector<double> &Parser::getStopToSchoolDistance() const {
    return stopToSchoolDistance;
}
