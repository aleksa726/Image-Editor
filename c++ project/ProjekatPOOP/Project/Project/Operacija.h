#pragma once
#include <string>
#include <algorithm>
#include "Pixel.h"
#include "Slika.h"

using namespace std;

class Greska_RGB_Operacija {
public:
	Greska_RGB_Operacija() { cout << endl << "   Error! Pogresno je uneta boja na koju se odnosi operacija!" << endl; }
};

class Operacija{
protected:
	int val;
	string boja1;
	bool kompozitna = false;
public:

	Operacija() = default;

	Operacija(int v, string b1 = "", bool komp = false);

	virtual void izvrsi(Pixel& p) = 0;
};

