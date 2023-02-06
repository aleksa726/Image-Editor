#include "Medijana.h"

Medijana::Medijana(Slika& glavna, bool komp, int v, string b1): Operacija(v, b1, komp) {
	for (auto& i : glavna.getSlojeve()) {
		if (i->getAktivan() == true) {
			sloj = *i;
			*i = izvrsi(glavna);
		}
	}
}

void Medijana::izvrsi(int i, Sloj& novi)
{
	int r1, r2, r3, r4, r5;
	int g1, g2, g3, g4, g5;
	int b1, b2, b3, b4, b5;
	int a1, a2, a3, a4, a5;

	if ((i - sloj.getWidth()) >= 0) {
		r1 = sloj.getPiksel(i - sloj.getWidth())->getR();
		g1 = sloj.getPiksel(i - sloj.getWidth())->getG();
		b1 = sloj.getPiksel(i - sloj.getWidth())->getB();
		a1 = sloj.getPiksel(i - sloj.getWidth())->getA();
	}
	else { r1 = -1; g1 = -1; b1 = -1; a1 = -1; }
	if ((i + sloj.getWidth()) < (sloj.getHeight() * sloj.getWidth())) {
		r2 = sloj.getPiksel(i + sloj.getWidth())->getR();
		g2 = sloj.getPiksel(i + sloj.getWidth())->getG();
		b2 = sloj.getPiksel(i + sloj.getWidth())->getB();
		a2 = sloj.getPiksel(i + sloj.getWidth())->getA();
	}
	else { r2 = -1; g2 = -1; b2 = -1; a2 = -1; }
	if (i % sloj.getWidth() != 0) {
		r3 = sloj.getPiksel(i - 1)->getR();
		g3 = sloj.getPiksel(i - 1)->getG();
		b3 = sloj.getPiksel(i - 1)->getB();
		a3 = sloj.getPiksel(i - 1)->getA();
	}
	else { r3 = -1; g3 = -1; b3 = -1; a3 = -1; }
	if ((i + 1) % sloj.getWidth() != 0) {
		r4 = sloj.getPiksel(i + 1)->getR();
		g4 = sloj.getPiksel(i + 1)->getG();
		b4 = sloj.getPiksel(i + 1)->getB();
		a4 = sloj.getPiksel(i + 1)->getA();
	}
	else { r4 = -1; g4 = -1; b4 = -1; a4 = -1; }

	r5 = sloj.getPiksel(i)->getR();
	g5 = sloj.getPiksel(i)->getG();
	b5 = sloj.getPiksel(i)->getB();
	a5 = sloj.getPiksel(i)->getA();

	int med1 = r5, med2 = g5, med3 = b5, med4 = a5;
	int cnt1 = 1, cnt2 = 1, cnt3 = 1, cnt4 = 1;

	if (r1 != -1) cnt1++, med1 += r1;
	if (r2 != -1) cnt1++, med1 += r2;
	if (r3 != -1) cnt1++, med1 += r3;
	if (r4 != -1) cnt1++, med1 += r4;

	if (g1 != -1) cnt2++, med2 += g1;
	if (g2 != -1) cnt2++, med2 += g2;
	if (g3 != -1) cnt2++, med2 += g3;
	if (g4 != -1) cnt2++, med2 += g4;

	if (b1 != -1) cnt3++, med3 += b1;
	if (b2 != -1) cnt3++, med3 += b2;
	if (b3 != -1) cnt3++, med3 += b3;
	if (b4 != -1) cnt3++, med3 += b4;

	if (a1 != -1) cnt4++, med4 += a1;
	if (a2 != -1) cnt4++, med4 += a2;
	if (a3 != -1) cnt4++, med4 += a3;
	if (a4 != -1) cnt4++, med4 += a4;

	med1 /= cnt1; med2 /= cnt2; med3 /= cnt3; med4 /= cnt4;

	novi.getPiksel(i)->setR(med1);
	novi.getPiksel(i)->setG(med2);
	novi.getPiksel(i)->setB(med3);
	novi.getPiksel(i)->setA(med4);
};

Sloj& Medijana::izvrsi(Slika glavna)
{
	Sloj* novi = new Sloj(sloj.getWidth(), sloj.getHeight());
	novi->setPikseli(sloj.getPikseli());
	if (sloj.getAktivan()) novi->aktiviraj();
	if (sloj.getVidljiv()) novi->postaniVidljiv();
	novi->setDirectory(sloj.getDirectory());
	novi->setProzirnost(sloj.getProzirnost());

	if (glavna.aktivneSelekcije() == true) {

		for (auto& j : glavna.getSel()) {
			if (j->getAktivan() == true) {

				for (auto& it : j->getPravougaonici()) {

					int poz = it->getY() * sloj.getWidth() + it->getX();
					for (int k = 0; k < it->getHeight() * it->getWidth(); k++) {

						if (((k % it->getWidth()) == 0) && (k != 0)) {
							poz += (sloj.getWidth() - it->getWidth());
						}

						izvrsi(poz, *novi);
						poz++;
					}
				}
			}
		}
	}
	else {
		int cnt = 0;
		for (auto& j : sloj.getPikseli()) {
			izvrsi(cnt, *novi);
			cnt++;
		}
	}

	return *novi;
}
