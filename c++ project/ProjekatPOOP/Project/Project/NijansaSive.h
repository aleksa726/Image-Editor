#pragma once
#include "Operacija.h"

using namespace std;

class NijansaSive : public Operacija {

public:
	
	NijansaSive(Slika& glavna, bool komp = false, string b1 = "", int v = 0);

	void izvrsi(Pixel& p) override;
};
