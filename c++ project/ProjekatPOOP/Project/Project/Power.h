#pragma once
#include "Operacija.h"

using namespace std;

class Power: public Operacija {

public:

	Power(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};