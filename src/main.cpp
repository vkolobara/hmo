//
// Created by vkolobara on 12/24/17.
//

#include <iostream>
#include "model/Parser.h"
#include "algorithm/StudentAssignment.h"

using namespace std;

int main(char **args) {
    Parser parser;
    parser.parse("../data/bus_routing/sbr1.txt");

    cout << parser.getBusCapacity() << endl;
    cout << parser.getMaxWalk() << endl;
    cout << parser.getBusStops().size() << endl;
    cout << parser.getStudents().size() << endl;
    cout << parser.getSchool()->getY() << endl;

    StudentAssignment assignment;

    assignment.assign(parser);

}