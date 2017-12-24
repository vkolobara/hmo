//
// Created by vkolobara on 12/24/17.
//

#ifndef HMO_STUDENT_H
#define HMO_STUDENT_H


#include "Coordinate.h"

class Student {
private:
    int id;
    Coordinate pos;
public:
    Student(int id, const Coordinate &pos);
    virtual ~Student();

    const Coordinate &getPos() const;
    int getId() const;

};


#endif //HMO_STUDENT_H
