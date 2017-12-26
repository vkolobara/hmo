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
    std::vector<int> busStopCount;
public:
    StudentAssignment();
    void assign(Parser parser);

    const std::map<int, int> &getAssignment() const;

    const std::vector<int> &getBusStopCount() const;
};


#endif //HMO_STUDENTASSIGNMENT_H
