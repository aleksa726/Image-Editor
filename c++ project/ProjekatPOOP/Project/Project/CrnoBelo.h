#pragma once
#include "Operacija.h"

using namespace std;

class CrnoBelo : public Operacija {

public:

	CrnoBelo(Slika& glavna, bool komp = false, int v = 0, string b1 = "");

	void izvrsi(Pixel& p) override;
};
