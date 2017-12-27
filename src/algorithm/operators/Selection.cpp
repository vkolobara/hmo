//
// Created by vkolobara on 12/27/17.
//

#include <cfloat>
#include "Selection.h"

TournamentSelection::TournamentSelection(uint tournamentSize) : tournamentSize(tournamentSize) {

}

vector<Solution> TournamentSelection::select(Population population) {

    std::uniform_int_distribution<> distr(0, population.getSize() - 1);

    set<int> selected;
    std::list<Solution> tournament;

    while(selected.size() < tournamentSize) {
        int index = distr(Random::eng);

        if (selected.count(index) == 0) {
            selected.insert(index);
            tournament.push_back(population.getPopulation().at(index));
        }
    }

    tournament.sort();

    return vector<Solution>(begin(tournament), end(tournament));
}
