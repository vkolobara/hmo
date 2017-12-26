//
// Created by vkolobara on 12/26/17.
//

#ifndef HMO_SOLUTION_H
#define HMO_SOLUTION_H

#include <vector>
#include "../model/Parser.h"

using namespace std;

class Solution {
private:
    vector<vector<int>> solution;
public:
    explicit Solution(vector<vector<int>> solution);

    static Solution initGRASP(Parser parser);

    const vector<vector<int>> &getSolution() const;
};


#endif //HMO_SOLUTION_H
