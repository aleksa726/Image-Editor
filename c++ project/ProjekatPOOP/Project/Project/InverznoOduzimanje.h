#pragma once
#include "Operacija.h"

using namespace std;

class InverznoOduzimanje : public Operacija {

public:

	InverznoOduzimanje(Slika& glavna, int v, string b1 = "", bool komp = false);

	void izvrsi(Pixel& p) override;
};
