#include "NijansaSive.h"

NijansaSive::NijansaSive(Slika& glavna, bool komp, string b1, int v): Operacija(v, b1, komp) {

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

void NijansaSive::izvrsi(Pixel& p)
{
	int as = (p.getR() + p.getB() + p.getG()) / 3;
	p.setR(as);
	p.setG(as);
	p.setB(as);
	p.setA(p.getA());
	if (kompozitna == false) p.zaokruzi();
}
