#include "Inverzija.h"

Inverzija::Inverzija(Slika& glavna, string b1, bool komp, int v): Operacija(v, b1, komp) {

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

void Inverzija::izvrsi(Pixel& p)
{
	if (boja1 == "r") {
		p.setR(255 - p.getR());
	}
	else if (boja1 == "g") {
		p.setG(255 - p.getG());
	}
	else if (boja1 == "b") {
		p.setB(255 - p.getB());
	}
	else if ((boja1 == "rg") || (boja1 == "gr")) {
		p.setR(255 - p.getR());
		p.setG(255 - p.getG());
	}
	else if ((boja1 == "rb") || (boja1 == "br")) {
		p.setR(255 - p.getR());
		p.setB(255 - p.getB());
	}
	else if ((boja1 == "gb") || (boja1 == "bg")) {
		p.setG(255 - p.getG());
		p.setB(255 - p.getB());
	}
	else if ((boja1 == "rgb") || (boja1 == "rbg") || (boja1 == "grb") || (boja1 == "gbr") || (boja1 == "brg") || (boja1 == "bgr")) {
		p.setR(255 - p.getR());
		p.setG(255 - p.getG());
		p.setB(255 - p.getB());
	}
	else {
		throw Greska_RGB_Operacija();
	}
	if (kompozitna == false) p.zaokruzi();
}
