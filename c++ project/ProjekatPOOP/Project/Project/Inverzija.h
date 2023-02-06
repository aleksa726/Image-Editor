#pragma once
#include "Operacija.h"

using namespace std;

class Inverzija : public Operacija {

public:

	Inverzija(Slika& glavna, string b1 = "", bool komp = false, int v = 0);

	void izvrsi(Pixel& p) override;
};

