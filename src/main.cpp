//
// Created by vkolobara on 12/24/17.
//

#include <iostream>
#include "model/Parser.h"
#include "algorithm/Solution.h"
#include "algorithm/Population.h"
#include "algorithm/operators/Selection.h"
#include <chrono>


using namespace std;

int main(int argc, char *argv[]) {


    Parser parser;
    parser.parse("../data/bus_routing/sbr1.txt");

    cout << parser.getBusCapacity() << endl;
    cout << parser.getMaxWalk() << endl;
    cout << parser.getBusStops().size() << endl;
    cout << parser.getStudents().size() << endl;
    cout << parser.getSchool()->getY() << endl;

    Population pop(10, parser);

    auto start = std::chrono::high_resolution_clock::now();

    TournamentSelection selection(4);
    for (auto sol : selection.select(pop)) {
        cout << sol.getFitness() << endl;
    }

    auto finish = std::chrono::high_resolution_clock::now();
    cout << "Total took " << (finish-start).count()/1000000000.0 << " seconds!" << endl;

}