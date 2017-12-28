//
// Created by vkolobara on 12/27/17.
//

#ifndef HMO_POPULATION_H
#define HMO_POPULATION_H


#include "Solution.h"

class Population {
private:
    vector<Solution> population;
    uint size;
public:
    explicit Population(uint size);
    void initPopulation(Parser parser, StudentAssignment assignment);

    const vector<Solution> &getPopulation() const;
    void addSolution(Solution solution);
    uint getSize() const;

};


#endif //HMO_POPULATION_H
