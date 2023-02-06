#include "Power.h"

Power::Power(Slika& glavna, int v, string b1, bool komp): Operacija(v, b1, komp) {

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

void Power::izvrsi(Pixel& p)
{
	if (boja1 == "r") {
		p.setR((int)pow(p.getR(), val));
	}
	else if (boja1 == "g") {
		p.setG((int)pow(p.getG(), val));
	}
	else if (boja1 == "b") {
		p.setB((int)pow(p.getB(), val));
	}
	else if ((boja1 == "rg") || (boja1 == "gr")) {
		p.setR((int)pow(p.getR(), val));
		p.setG((int)pow(p.getG(), val));
	}
	else if ((boja1 == "rb") || (boja1 == "br")) {
		p.setR((int)pow(p.getR(), val));
		p.setB((int)pow(p.getB(), val));
	}
	else if ((boja1 == "gb") || (boja1 == "bg")) {
		p.setG((int)pow(p.getG(), val));
		p.setB((int)pow(p.getB(), val));
	}
	else if ((boja1 == "rgb") || (boja1 == "rbg") || (boja1 == "grb") || (boja1 == "gbr") || (boja1 == "brg") || (boja1 == "bgr")) {
		p.setR((int)pow(p.getR(), val));
		p.setG((int)pow(p.getG(), val));
		p.setB((int)pow(p.getB(), val));
	}
	else {
		throw Greska_RGB_Operacija();
	}
	if (kompozitna == false) p.zaokruzi();
}
