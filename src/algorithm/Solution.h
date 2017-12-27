//
// Created by vkolobara on 12/26/17.
//

#ifndef HMO_SOLUTION_H
#define HMO_SOLUTION_H

#include <vector>
#include "../model/Parser.h"
#include <utility>
#include <queue>
#include <set>
#include "StudentAssignment.h"
#include "Random.h"

using namespace std;

class Solution {
private:
    vector<int> solution;
    Parser parser;
public:
    explicit Solution(vector<int> solution, Parser parser);

    double getFitness() const;

    static Solution initGRASP(Parser parser);

    const vector<int> &getSolution() const;

    const Parser &getParser() const;
    bool operator <(const Solution & obj) const;
};


#endif //HMO_SOLUTION_H
