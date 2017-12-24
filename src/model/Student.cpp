//
// Created by vkolobara on 12/24/17.
//

#include "Student.h"

Student::Student(int id, const Coordinate &pos) : id(id), pos(pos) {}

Student::~Student() {}

const Coordinate &Student::getPos() const {
    return pos;
}

int Student::getId() const {
    return id;
};
