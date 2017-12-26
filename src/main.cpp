//
// Created by vkolobara on 12/24/17.
//

#include <iostream>
#include "model/Parser.h"
#include "algorithm/StudentAssignment.h"
#include "algorithm/Solution.h"

using namespace std;

int main(char **args) {
    Parser parser;
    parser.parse("../data/bus_routing/sbr10.txt");

    cout << parser.getBusCapacity() << endl;
    cout << parser.getMaxWalk() << endl;
    cout << parser.getBusStops().size() << endl;
    cout << parser.getStudents().size() << endl;
    cout << parser.getSchool()->getY() << endl;

    Solution sol = Solution::initGRASP(parser);

    double totalDistance = 0;
    for (auto route : sol.getSolution()) {
        int studentCount = 0;
        double distance = parser.getStopToSchoolDistance()[route[0]] + parser.getStopToSchoolDistance()[route[route.size()-1]];
        for (int i=1; i<route.size(); i++) {
            distance += Coordinate::euclideanDistance(parser.getBusStops()[route[i-1]], parser.getBusStops()[route[i]]);
            cout << route[i] << " ";
        }
        cout << endl << "DISTANCE ROUTE: " << distance << endl;

        totalDistance+=distance;
    }

    cout << endl << "TOTAL DISTANCE: " << totalDistance << endl;


}