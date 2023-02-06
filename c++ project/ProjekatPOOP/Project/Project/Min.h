#pragma once
#include "Operacija.h"

using namespace std;

class Min : public Operacija {

public:

	Min(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};