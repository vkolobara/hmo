//
// Created by vkolobara on 12/26/17.
//

#include <algorithm>
#include <random>
#include <iostream>
#include "Mutation.h"

AdjustRouteMutation::AdjustRouteMutation(double mutFactor) : mutFactor(mutFactor) {}

Solution AdjustRouteMutation::mutate(Solution sol) {
    auto gen = std::uniform_real_distribution<>(0,1);


    vector<int> mutated;

    const vector<int> &currSolution = sol.getSolution();

    auto busStopCount = sol.getAssignment().getBusStopCount();
    auto busCapacity = sol.getParser().getBusCapacity();

    vector<int> indice;

    vector<int> counts;

    int count = 0;
    for (int i=0; i<currSolution.size(); i++) {
        if (currSolution[i] == 0) {
            indice.push_back(i);
            counts.push_back(count);
            count = 0;
        }
        count+=busStopCount[currSolution[i]-1];
        mutated.push_back(currSolution[i]);
    }

    auto currIndex = 0;


    for (int i=0; i<indice.size(); i++) {
        if (gen(Random::eng) <= mutFactor) {
            shuffle(mutated.begin()+currIndex, mutated.begin()+indice[i]-1, Random::eng);
        }
        currIndex = indice[i]+1;
    }

    return Solution(mutated, sol.getParser(), sol.getAssignment());
}

CombineRouteMutation::CombineRouteMutation(double mutFactor) : mutFactor(mutFactor) {}

Solution CombineRouteMutation::mutate(Solution sol) {
    auto gen = std::uniform_real_distribution<>(0,1);


    vector<int> mutated;

    const vector<int> &currSolution = sol.getSolution();

    auto busStopCount = sol.getAssignment().getBusStopCount();
    auto busCapacity = sol.getParser().getBusCapacity();

    vector<int> indice;

    vector<int> counts;

    int count = 0;
    for (int i=0; i<currSolution.size(); i++) {
        if (currSolution[i] == 0) {
            indice.push_back(i);
            counts.push_back(count);
            count = 0;
        }
        count+=busStopCount[currSolution[i]-1];
        mutated.push_back(currSolution[i]);
    }

    return Solution(std::vector<int>(), Parser(), StudentAssignment());
}
