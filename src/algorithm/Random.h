//
// Created by vkolobara on 12/27/17.
//

#ifndef HMO_RANDOM_H
#define HMO_RANDOM_H


#include <random>

class Random {
public:
    static std::random_device rd; // obtain a random number from hardware
    static std::mt19937 eng; // seed the generator

};

inline std::random_device Random::rd; // obtain a random number from hardware
inline std::mt19937 Random::eng(Random::rd());


#endif //HMO_RANDOM_H
