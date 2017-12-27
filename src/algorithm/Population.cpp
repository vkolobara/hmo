//
// Created by vkolobara on 12/27/17.
//

#include "Population.h"

Population::Population(uint size, Parser parser) : size(size) {
    initPopulation(parser);
}

void Population::initPopulation(Parser parser) {

    for (uint i=0; i<size; i++) {
        population.push_back(Solution::initGRASP(parser));
    }

}

const vector<Solution> &Population::getPopulation() const {
    return population;
}

uint Population::getSize() const {
    return size;
}
