//
// Created by vkolobara on 12/26/17.
//

#include <random>
#include <utility>
#include <queue>
#include <set>
#include "Solution.h"
#include "StudentAssignment.h"

Solution Solution::initGRASP(Parser parser) {
    std::random_device rd; // obtain a random number from hardware
    std::mt19937 eng(rd()); // seed the generator
    auto busCapacity = parser.getBusCapacity();
    auto maxWalk = parser.getMaxWalk();
    const auto &busStops = parser.getBusStops();
    const auto &students = parser.getStudents();
    const auto &school = parser.getSchool();
    const auto &stopDistances = parser.getStopToSchoolDistance();
    const auto &studentDistances = parser.getStudentToStopDistance();

    StudentAssignment assignment;
    assignment.assign(parser);

    const auto &busStopCount = assignment.getBusStopCount();

    vector<vector<int>> solution;
    vector<int> studentsPerRoute;

    map<pair<int, int>, double> savingsMatrix;
    map<int, int> busStopOnRoute;

    auto cmp = [&](pair<int, int> left, pair<int, int> right) {
        return savingsMatrix[left] > savingsMatrix[right];
    };

    std::priority_queue<int, std::vector<pair<int, int>>, decltype(cmp)> queue(cmp);

    for (int i = 0; i < busStopCount.size(); i++) {
        auto value = busStopCount[i];

        if (value > 0) {
            solution.emplace_back(vector<int>({i}));
            studentsPerRoute.push_back(value);
            busStopOnRoute[i] = (int) solution.size() - 1;
        }

        for (int j = 0; j < i; j++) {
            auto p = pair<int, int>(i, j);

            savingsMatrix[p] = stopDistances[i] + stopDistances[j]
                               - Coordinate::euclideanDistance(busStops[i], busStops[j]);
            queue.push(p);
        }
    }

    vector<pair<int, int>> sorted(queue.size());

    while (!queue.empty()) {
        sorted.push_back(queue.top());
        queue.pop();
    }

    while (!sorted.empty()) {
        int alpha = min(max((int) sorted.size() / 10, 10), (int) sorted.size());

        std::uniform_int_distribution<> distr(0, alpha - 1);

        int index = distr(eng);
        auto curr = sorted[index];

        auto i = curr.first;
        auto j = curr.second;

        if (studentsPerRoute[busStopOnRoute[i]] + studentsPerRoute[busStopOnRoute[j]] <= busCapacity
            && studentsPerRoute[busStopOnRoute[i]] > 0
            && studentsPerRoute[busStopOnRoute[j]] > 0
                && i!=j) {

            if (solution[busStopOnRoute[i]].front() == i && solution[busStopOnRoute[j]].back() == j) {

                auto route = busStopOnRoute[j];

                studentsPerRoute[busStopOnRoute[i]] += studentsPerRoute[busStopOnRoute[j]];
                studentsPerRoute[busStopOnRoute[j]] = 0;

                for (auto elem : solution[busStopOnRoute[j]]) {
                    busStopOnRoute[elem] = busStopOnRoute[i];
                    solution[busStopOnRoute[i]].push_back(elem);
                }

                solution[route].clear();

            } else if (solution[busStopOnRoute[j]].front() == j && solution[busStopOnRoute[i]].back() == i) {
                auto route = busStopOnRoute[i];

                studentsPerRoute[busStopOnRoute[j]] += studentsPerRoute[busStopOnRoute[i]];
                studentsPerRoute[busStopOnRoute[i]] = 0;

                for (auto elem : solution[busStopOnRoute[i]]) {
                    busStopOnRoute[elem] = busStopOnRoute[j];
                    solution[busStopOnRoute[j]].push_back(elem);
                }

                solution[route].clear();

            }

        }

        sorted.erase(sorted.begin() + index);

    }

    vector<vector<int>> final_solution;

    for (int i = 0; i < solution.size(); i++) {
        if (studentsPerRoute[i] > 0) {
            final_solution.push_back(solution[i]);
        }
    }

    return Solution(final_solution, parser);
}

Solution::Solution(vector<vector<int>> solution, Parser parser) : solution(std::move(solution)), parser(parser) {}

const vector<vector<int>> &Solution::getSolution() const {
    return solution;
}

double Solution::getFitness() {
    double fitness = 0;
    auto busStopDistances = parser.getStopToSchoolDistance();
    auto busStops = parser.getBusStops();
    for (auto route : solution) {
        for (int i=1; i<route.size(); i++) {
            fitness += Coordinate::euclideanDistance(busStops[route[i]], busStops[route[i-1]]);
        }
        fitness += busStopDistances[route[0]] + busStopDistances[route[route.size()-1]];
    }
    return fitness;
}
