//
// Created by vkolobara on 12/24/17.
//

#ifndef HMO_STUDENTONBUSSTOP_H
#define HMO_STUDENTONBUSSTOP_H


#include <map>
#include <vector>
#include "../model/Student.h"
#include "../model/BusStop.h"
#include "../model/Parser.h"

class StudentOnBusStop {
private:
    std::map<int, int> studentToBusStopMapping;
public:
    void createMapping(std::shared_ptr<Parser> parser);
};


#endif //HMO_STUDENTONBUSSTOP_H
