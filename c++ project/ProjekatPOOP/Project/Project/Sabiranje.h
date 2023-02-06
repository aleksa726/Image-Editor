#pragma once
#include "Operacija.h"
#include <algorithm>
#include <iterator>

using namespace std;

class Pravougaonik;

class Sabiranje : public Operacija {

public:

	Sabiranje(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};

