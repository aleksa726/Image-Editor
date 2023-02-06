#pragma once
#include "Operacija.h"
#include "Sloj.h"
#include "Slika.h"

using namespace std;

class Sloj;

class Medijana: public Operacija{
	Sloj sloj;
public:

	Medijana(Slika& glavna, bool komp = false, int v = 0, string b1 = "");

	void izvrsi(Pixel& p) {};

	void izvrsi(int, Sloj&);

	Sloj& izvrsi(Slika);
};

