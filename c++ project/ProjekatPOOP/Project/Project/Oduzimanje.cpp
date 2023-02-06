#include "Oduzimanje.h"

Oduzimanje::Oduzimanje(Slika& glavna, int v, string b1, bool komp): Operacija(v, b1, komp) {

	for (auto& i : glavna.getSlojeve()) {
		if (i->getAktivan() == true) {
			if (glavna.aktivneSelekcije() == true) {

				for (auto& j : glavna.getSel()) {
					if (j->getAktivan() == true) {

						for (auto& it : j->getPravougaonici()) {

							int poz = it->getY() * i->getWidth() + it->getX();
							for (int k = 0; k < it->getHeight() * it->getWidth(); k++) {

								if (((k % it->getWidth()) == 0) && (k != 0)) {
									poz += (i->getWidth() - it->getWidth());
								}

								izvrsi(*i->getPiksel(poz));
								poz++;
							}
						}
					}
				}
			}
			else {
				for (auto& j : i->getPikseli()) {
					izvrsi(*j);
				}
			}
		}
	}
}

void Oduzimanje::izvrsi(Pixel& p)
{
	if (boja1 == "r") {
		p.setR(p.getR() - val);
	}
	else if (boja1 == "g") {
		p.setG(p.getG() - val);
	}
	else if (boja1 == "b") {
		p.setB(p.getB() - val);
	}
	else if ((boja1 == "rg") || (boja1 == "gr")) {
		p.setR(p.getR() - val);
		p.setG(p.getG() - val);
	}
	else if ((boja1 == "rb") || (boja1 == "br")) {
		p.setR(p.getR() - val);
		p.setB(p.getB() - val);
	}
	else if ((boja1 == "gb") || (boja1 == "bg")) {
		p.setG(p.getG() - val);
		p.setB(p.getB() - val);
	}
	else if ((boja1 == "rgb") || (boja1 == "rbg") || (boja1 == "grb") || (boja1 == "gbr") || (boja1 == "brg") || (boja1 == "bgr")) {
		p.setR(p.getR() - val);
		p.setG(p.getG() - val);
		p.setB(p.getB() - val);
	}
	else {
		throw Greska_RGB_Operacija();
	}
	if (kompozitna == false) p.zaokruzi();
}
