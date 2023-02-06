#pragma once
#include "Operacija.h"

using namespace std;

class Max : public Operacija {

public:

	Max(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};