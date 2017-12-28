//
// Created by vkolobara on 12/26/17.
//

#ifndef HMO_MUTATION_H
#define HMO_MUTATION_H


#include "../Solution.h"

class Mutation {
public:
    virtual Solution mutate(Solution sol) = 0;
};

class AdjustRouteMutation : public Mutation {
private:
    double mutFactor;
public:
    explicit AdjustRouteMutation(double mutFactor);
    Solution mutate(Solution sol) override;
};

class CombineRouteMutation : public Mutation {
private:
    double mutFactor;
public:
    explicit CombineRouteMutation(double mutFactor);
    Solution mutate(Solution sol) override;
};

#endif //HMO_MUTATION_H
