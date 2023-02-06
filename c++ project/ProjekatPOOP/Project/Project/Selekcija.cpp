#include "Selekcija.h"

Selekcija::Selekcija(string i, set<Pravougaonik*> pr)
{
	this->ime = i;
	this->pravougaonici = pr;
}

void Selekcija::popuniBojom(int r, int g, int b)
{
	if ((r < 0) || (r > 255) || (g < 0) || (g > 255) || (b < 0) || (b > 255)) throw GreskaVrednostPiksela();
	this->R = r;
	this->G = g;
	this->B = b;
}

set<Pravougaonik*> Selekcija::getPravougaonici() const
{
	return this->pravougaonici;
}

ostream& operator<<(ostream& out, const Selekcija& s)
{
	out << s.ime;
	if (s.aktivna) out << " aktivna; ";
	else out << " neaktivna; ";
	for (auto i : s.pravougaonici) out << *i << " ";
	return out;
}
