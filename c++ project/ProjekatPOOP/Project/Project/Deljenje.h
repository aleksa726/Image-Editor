#pragma once
#include "Operacija.h"

using namespace std;

class Deljenje : public Operacija {

public:

	Deljenje(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};

