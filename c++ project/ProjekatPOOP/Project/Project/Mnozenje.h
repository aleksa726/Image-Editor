#pragma once
#include "Operacija.h"

using namespace std;

class Mnozenje : public Operacija {

public:

	Mnozenje(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};
