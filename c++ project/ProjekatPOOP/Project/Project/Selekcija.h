#pragma once
#include <set>
#include "Pravougaonik.h"
#include <string>
#include <vector>

using namespace std;

class GreskaVrednostPiksela {
public:
	GreskaVrednostPiksela() { cout << endl << endl << "   Error! Uneta je pogresna vrednost piksela!" << endl; }
};


/*class IteratorPravougaonika {
	Pravougaonik* tekuci;
public:

	IteratorPravougaonika(Pravougaonik* tek) {
		this->tekuci = tek;
	}

	bool operator != (const IteratorPravougaonika& it) {
		return tekuci != it.tekuci;
	}

	IteratorPravougaonika& operator++() {
		tekuci++;
		return *this;
	}

	IteratorPravougaonika operator++(int) {
		IteratorPravougaonika old(*this);
		++* this;
		return old;
	}

	Pravougaonik& operator*() {
		return *tekuci;
	}
};*/

class Pravougaonik;

class Selekcija{

	set<Pravougaonik*> pravougaonici;

	string ime;
	bool aktivna = true;

	int R = -1, G = -1, B = -1;

public:

	Selekcija(string i, set<Pravougaonik*> pr);
	void popuniBojom(int r, int g, int b);

	set<Pravougaonik*> getPravougaonici() const;
	void aktiviraj() { this->aktivna = true; }
	void deaktiviraj() { this->aktivna = false; }
	bool getAktivan() { return this->aktivna; }
	string getIme() const { return this->ime; }
	int getR()const { return R; }
	int getG()const { return G; }
	int getB()const { return B; }

	friend ostream& operator << (ostream& out, const Selekcija& s);

};

