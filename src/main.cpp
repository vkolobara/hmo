//
// Created by vkolobara on 12/24/17.
//

#include <iostream>
#include "model/Parser.h"
#include "algorithm/Solution.h"
#include "algorithm/Population.h"
#include "algorithm/operators/Selection.h"
#include "algorithm/Algorithm.h"
#include <chrono>


using namespace std;

int main(int argc, char *argv[]) {

    auto start = std::chrono::high_resolution_clock::now();


    Parser parser;
    parser.parse("../data/bus_routing/sbr1.txt");

    cout << parser.getBusCapacity() << endl;
    cout << parser.getMaxWalk() << endl;
    cout << parser.getBusStops().size() << endl;
    cout << parser.getStudents().size() << endl;
    cout << parser.getSchool()->getY() << endl;

    shared_ptr<Selection> selection = make_shared<TournamentSelection>(3);
    shared_ptr<Mutation> mutation = make_shared<AdjustRouteMutation>(0.2);

    Algorithm algo(30);

    algo.execute(parser, selection, mutation);

    auto finish = std::chrono::high_resolution_clock::now();
    cout << "Total took " << (finish-start).count()/1000000000.0 << " seconds!" << endl;

}