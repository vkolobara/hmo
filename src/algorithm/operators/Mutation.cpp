//
// Created by vkolobara on 12/26/17.
//

#include "Mutation.h"

SimpleMutation::SimpleMutation(double mutFactor) : mutFactor(mutFactor) {}

Solution SimpleMutation::mutate(Solution sol) {

    vector<int> mutated;

    const vector<int> &currSolution = sol.getSolution();





    return Solution(mutated, sol.getParser());
}
