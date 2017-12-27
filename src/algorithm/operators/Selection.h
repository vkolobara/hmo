//
// Created by vkolobara on 12/27/17.
//

#ifndef HMO_SELECTION_H
#define HMO_SELECTION_H

#include <list>
#include "../Solution.h"
#include "../Population.h"

class Selection {
public:
    virtual vector<Solution> select(Population population) = 0;
};

class TournamentSelection : public Selection {
private:
    uint tournamentSize;
public:
    explicit TournamentSelection(uint tournamentSize);
    vector<Solution> select(Population population) override;
};


#endif //HMO_SELECTION_H
