//
// Created by vkolobara on 12/25/17.
//

#include "StudentAssignment.h"

void StudentAssignment::assign(Parser parser) {
    auto busCapacity = parser.getBusCapacity();
    auto maxWalk = parser.getMaxWalk();
    const auto &busStops = parser.getBusStops();
    const auto &students = parser.getStudents();
    const auto &school = parser.getSchool();
    const auto &stopDistances = parser.getStopToSchoolDistance();
    const auto &studentDistances = parser.getStudentToStopDistance();

    auto alpha = 20;

    auto cmp = [&](int left, int right) {
        return stopDistances[left] < stopDistances[right];
    };

    busStopCount = std::vector<int>(busStops.size(), 0);

    for (int i=0; i<students.size(); i++) {
        std::priority_queue<int, std::vector<int>, decltype(cmp)> queue(cmp);

        for (int j=0; j<busStops.size(); j++) {
            if (maxWalk >= studentDistances[i][j]) {
                queue.push(j);
            }
        }

        if (queue.size() == 1) {
            assignment[i] = queue.top();
        } else {
            std::vector<int> possibilities;

            for (int j=0; j<alpha && !queue.empty(); j++) {
                possibilities.push_back(queue.top());
                queue.pop();
            }
            std::uniform_int_distribution<> distr(0, possibilities.size()-1);
            int ind = distr(Random::eng);
            int sel = possibilities[ind];

            while(true) {
                if (busStopCount.at(sel) < busCapacity) {
                    assignment[i] = sel;
                    break;
                } else {
                    possibilities.erase(possibilities.begin()+ind);
                    ind = std::max(ind-1, 0);
                    sel = possibilities[ind];
                }
            }
        }
        busStopCount[assignment[i]]++;


    }
}

StudentAssignment::StudentAssignment() {

}

const std::map<int, int> &StudentAssignment::getAssignment() const {
    return assignment;
}

const std::vector<int> &StudentAssignment::getBusStopCount() const {
    return busStopCount;
}
