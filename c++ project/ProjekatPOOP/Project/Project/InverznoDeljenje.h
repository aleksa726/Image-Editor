#pragma once
#include "Operacija.h"

using namespace std;

class InverznoDeljenje : public Operacija {

public:

	InverznoDeljenje(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};
