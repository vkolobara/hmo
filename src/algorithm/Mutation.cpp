//
// Created by vkolobara on 12/26/17.
//

#include "Mutation.h"

SimpleMutation::SimpleMutation(double mutFactor) : mutFactor(mutFactor) {}

Solution SimpleMutation::mutate(Solution sol) {

    vector<vector<int>> mutated;

    return Solution(mutated, sol.getParser());
}
