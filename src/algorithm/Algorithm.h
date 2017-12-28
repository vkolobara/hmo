//
// Created by vkolobara on 12/28/17.
//

#ifndef HMO_ALGORITHM_H
#define HMO_ALGORITHM_H


#include "Solution.h"
#include "operators/Selection.h"
#include "operators/Mutation.h"

class Algorithm {
private:
    uint popSize;

public:
    explicit Algorithm(uint popSize);

    Solution execute(Parser parser, shared_ptr<Selection> selection, shared_ptr<Mutation> mutation);

};


#endif //HMO_ALGORITHM_H
