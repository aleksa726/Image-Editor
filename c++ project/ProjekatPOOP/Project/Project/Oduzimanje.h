#pragma once
#include "Operacija.h"

using namespace std;

class Oduzimanje : public Operacija {

public:

	Oduzimanje(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};
