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
    StudentAssignment assignment;
public:
    Solution(vector<int> solution, Parser parser, StudentAssignment asignment);

    double getFitness() const;

    static Solution initGRASP(Parser parser, StudentAssignment assignment);

    const vector<int> &getSolution() const;

    const Parser &getParser() const;
    bool operator <(const Solution & obj) const;

    const StudentAssignment &getAssignment() const;
};


#endif //HMO_SOLUTION_H
