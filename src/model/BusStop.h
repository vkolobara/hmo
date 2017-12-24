//
// Created by vkolobara on 12/24/17.
//

#ifndef HMO_BUSSTOP_H
#define HMO_BUSSTOP_H


#include "Coordinate.h"

class BusStop {
private:
    int id;
    Coordinate pos;
public:
    BusStop(int id, const Coordinate &pos);

    int getId() const;
    const Coordinate &getPos() const;
};


#endif //HMO_BUSSTOP_H
