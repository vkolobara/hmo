//
// Created by vkolobara on 12/24/17.
//

#include <cmath>
#include "Coordinate.h"

Coordinate::Coordinate(double x, double y) : x(x), y(y) {}

double const Coordinate::getX()const {
    return x;
};

double const Coordinate::getY()const {
    return y;
};

double Coordinate::euclideanDistance(Coordinate c1, Coordinate c2) {
    return sqrt(pow(c1.x - c2.x, 2) + pow(c1.y - c2.y, 2));
}