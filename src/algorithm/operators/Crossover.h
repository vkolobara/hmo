//
// Created by vkolobara on 12/28/17.
//

#ifndef HMO_CROSSOVER_H
#define HMO_CROSSOVER_H


#include "../Solution.h"

class Crossover {
public:
    virtual Solution crossover(Solution parent1, Solution parent2);
};


#endif //HMO_CROSSOVER_H
