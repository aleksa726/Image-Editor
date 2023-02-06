#include "CrnoBelo.h"

CrnoBelo::CrnoBelo(Slika& glavna, bool komp, int v, string b1): Operacija(v, b1, komp) {

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

void CrnoBelo::izvrsi(Pixel& p)
{
	int as = (p.getR() + p.getB() + p.getG()) / 3;
	if (as < 127) {
		p.setR(0);
		p.setG(0);
		p.setB(0);
	}
	else {
		p.setR(255);
		p.setG(255);
		p.setB(255);
	}
	if (kompozitna == false) p.zaokruzi();
}