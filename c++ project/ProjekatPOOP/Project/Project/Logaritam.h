#pragma once
#include "Operacija.h"

using namespace std;

class Logaritam : public Operacija {

public:

	Logaritam(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};

