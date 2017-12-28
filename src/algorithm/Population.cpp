//
// Created by vkolobara on 12/27/17.
//

#include "Population.h"

Population::Population(uint size) : size(size) {
}

void Population::initPopulation(Parser parser, StudentAssignment assignment) {
    for (uint i=0; i<size; i++) {
        addSolution(Solution::initGRASP(parser, assignment));
    }

}

const vector<Solution> &Population::getPopulation() const {
    return population;
}

uint Population::getSize() const {
    return size;
}

void Population::addSolution(Solution solution) {
    population.push_back(solution);
}
