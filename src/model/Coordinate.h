//
// Created by vkolobara on 12/24/17.
//

#ifndef HMO_COORDINATE_H
#define HMO_COORDINATE_H

#include <cmath>

class Coordinate {
private:
    double x, y;
public:
    Coordinate(double x, double y);

    const double getX()const;
    const double getY()const;

    static double euclideanDistance(Coordinate c1, Coordinate c2);

};


#endif //HMO_COORDINATE_H
