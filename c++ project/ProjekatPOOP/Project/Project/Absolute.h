#pragma once
#include "Operacija.h"
#include <math.h>

using namespace std;

class Absolute : public Operacija {
public:
	
	Absolute(Slika& glavna, string b1 = "", bool komp = false, int v = 0);

	void izvrsi(Pixel& p) override;
};
