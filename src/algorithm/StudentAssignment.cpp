//
// Created by vkolobara on 12/25/17.
//

#include <queue>
#include <iostream>
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

    std::vector<int> busStopCount = {0};

    for (int i=0; i<students.size(); i++) {
        std::priority_queue<int, std::vector<int>, decltype(cmp)> queue(cmp);
        std::cout << "PRIJE " << std::endl;

        for (int j=0; j<busStops.size(); j++) {
            if (maxWalk >= studentDistances[i][j]) {
                queue.push(j);
            }
        }

        std::cout << "TU: " << i << std::endl;
        std::cout << queue.size() << std::endl;
        if (queue.size() == 1) {
            assignment[i] = queue.top();
            busStopCount[queue.top()]++;
        } else {
            std::vector<int> possibilities;
            for (int i=0; i<alpha && !queue.empty(); i++) {
                possibilities.push_back(queue.top());
                queue.pop();
            }
            while (true) {
                auto sel = possibilities[rand() % possibilities.size()];
                std::cout << possibilities.size();
                std::cout << "SELECTED: " << sel << std::endl;
                if (busStopCount[sel] <= busCapacity) {
                    assignment[i] = sel;
                    busStopCount[sel]++;
                    break;
                }
            }
        }

    }

    for (auto elem : assignment) {
        std::cout << elem.first << " " << elem.second << std::endl;
    }




}

StudentAssignment::StudentAssignment() {

}
