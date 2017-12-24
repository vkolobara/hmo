//
// Created by vkolobara on 12/24/17.
//

#include "BusStop.h"

int BusStop::getId() const {
    return id;
}

const Coordinate &BusStop::getPos() const {
    return pos;
}

BusStop::BusStop(int id, const Coordinate &pos) : id(id), pos(pos) {}

