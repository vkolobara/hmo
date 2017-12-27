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
    void initPopulation(Parser parser);
public:
    explicit Population(uint size, Parser parser);

    const vector<Solution> &getPopulation() const;

    uint getSize() const;

};


#endif //HMO_POPULATION_H
