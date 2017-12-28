//
// Created by vkolobara on 12/28/17.
//

#include <iostream>
#include <algorithm>
#include "Algorithm.h"

Algorithm::Algorithm(uint popSize) : popSize(popSize) {}

Solution Algorithm::execute(Parser parser, shared_ptr<Selection> selection, shared_ptr<Mutation> mutation) {

    StudentAssignment assignment;
    assignment.assign(parser);


    Population pop(popSize);
    pop.initPopulation(parser, assignment);

    for (int i=0; i<1000; i++) {
        Population newPop(popSize);

        double bestFit = 1911111;
        for (int j=0; j<popSize; j++) {
            Solution sol = mutation->mutate(pop.getPopulation().at(j));
            newPop.addSolution(sol);
            if (sol.getFitness() < bestFit) bestFit = sol.getFitness();
        }

        std::cout << "BEST FITNESS AT " << i << " : " << bestFit << endl;

        pop=newPop;
    }


    return pop.getPopulation()[0];
}
