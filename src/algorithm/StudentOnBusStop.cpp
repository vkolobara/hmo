//
// Created by vkolobara on 12/24/17.
//

#include "StudentOnBusStop.h"

void StudentOnBusStop::createMapping(std::shared_ptr<Parser> parser) {

    std::vector<Student> students = parser->getStudents();
    std::vector<BusStop> busStops = parser->getBusStops();
    double maxWalk = parser->getMaxWalk();
    unsigned int busCapacity = parser->getBusCapacity();

    double studentToBusStopDistances[students.size()][busStops.size()] = {-1};

    for (int i=0; i<students.size(); i++) {
        int pos = -1;
        int cnt = 0;
        for (int j=0; j<busStops.size(); j++) {
            double dst = Coordinate::euclideanDistance(students[i].getPos(), busStops[j].getPos());

            if (dst <= maxWalk) {
                cnt++;
                pos = j;
                studentToBusStopDistances[i][j] = dst;
            }
        }

        // Assign bus stops to students which can walk only to one of them
        if (cnt == 1) {
            studentToBusStopMapping[i] = pos;
        }
    }
}
