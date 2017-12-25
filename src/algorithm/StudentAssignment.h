//
// Created by vkolobara on 12/25/17.
//

#ifndef HMO_STUDENTASSIGNMENT_H
#define HMO_STUDENTASSIGNMENT_H


#include <map>
#include "../model/Parser.h"

class StudentAssignment {
private:
    std::map<int,int> assignment;
public:
    StudentAssignment();
    void assign(Parser parser);

};


#endif //HMO_STUDENTASSIGNMENT_H
