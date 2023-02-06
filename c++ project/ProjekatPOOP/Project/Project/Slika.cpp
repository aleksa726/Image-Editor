#include "Slika.h"

Sloj* Slika::getSloj(int a)
{
	if (this->slojevi.size() != 0) {
		if ((a < 0) || (a >= slojevi.size())) throw GreskaNeispravanIndeks();
		return slojevi[a];
	}
	else return nullptr;
}

Selekcija& Slika::getSelekcija(int a)
{
	if ((a < 0) || (a >= selekcije.size())) throw GreskaNeispravanIndeks();
	return *selekcije[a];
}

vector<Sloj*> Slika::getSlojeve() const
{
	return this->slojevi; 
}

vector<Selekcija*> Slika::getSel() const
{
	return this->selekcije;
}

void Slika::dodajSloj(string directory) 
{
	try {
		Sloj* sl = new Sloj(directory);

		if (this->getSloj(0) != nullptr) {

			if (sl->getWidth() < slojevi[0]->getWidth()) sl->dopuni_sirinu(slojevi[0]->getWidth());
			else if (sl->getWidth() > slojevi[0]->getWidth()) {
				for (auto& i : slojevi)i->dopuni_sirinu(sl->getWidth());
			}

			if (sl->getHeight() < slojevi[0]->getHeight()) sl->dopuni_visinu(slojevi[0]->getHeight());
			else if (sl->getHeight() > slojevi[0]->getHeight()) {
				for (auto& i : slojevi)i->dopuni_visinu(sl->getHeight());
			}

		}
		slojevi.push_back(sl);
	}
	catch (GreskaFormat& gr) {}
}

void Slika::dodajSloj(int w, int h) 
{
		

		Sloj* sl = new Sloj(w, h);

		if (this->getSloj(0) != nullptr) {

			if (sl->getWidth() < slojevi[0]->getWidth()) sl->dopuni_sirinu(slojevi[0]->getWidth());
			else if (sl->getWidth() > slojevi[0]->getWidth()) {
				for (int i = 0; i < slojevi.size(); i++) {
					for (auto& i : slojevi)i->dopuni_sirinu(sl->getWidth());
				}
			}

			if (sl->getHeight() < slojevi[0]->getHeight()) sl->dopuni_visinu(slojevi[0]->getHeight());
			else if (sl->getHeight() > slojevi[0]->getHeight()) {
				for (int i = 0; i < slojevi.size(); i++) {
					for (auto& i : slojevi)i->dopuni_visinu(sl->getHeight());
				}
			}

		}
		slojevi.push_back(sl);
}

void Slika::dodajSelekciju(Selekcija* s)
{
	this->selekcije.push_back(s);
}

void Slika::pisi_slojeve()
{
	if (slojevi.empty()) {
		cout << "   -Nema slojeva!" << endl;
	}
	else {
		int cnt = 0;
		for (auto& i : slojevi) {										// OVDE BI TREBALO FOR EACH SA LAMBDA FUNKCIJOM			
			cout << "   " << cnt++ << ".  " << *i << endl;
		}
	}
}

void Slika::pisi_selekcije()
{
	cout << endl;
	if (selekcije.empty()) {
		cout << "   -Nema selekcija!" << endl;
	}
	else {
		int cnt = 0;
		for (auto& i : selekcije) {					// OVDE BI TREBALO FOR EACH SA LAMBDA FUNKCIJOM		
			cout << "   " << cnt++ << ".  " << *i << endl;
		}
	}
}

void Slika::exportuj(string directory)
{
	int cnt = 0;
	Sloj* expSloj = new Sloj(slojevi.front()->getWidth(), slojevi.front()->getHeight());
	for (int i = slojevi.size() - 1; i >= 0; i--) {				// obrnuti iterator
		if (slojevi[i]->getVidljiv() == true) {
			if (slojevi[i]->getProzirnost() == 100) {
				if (cnt == 0) {
					cnt++;
					expSloj = slojevi[i];
				}
				else {
					cnt++;
					expSloj->stopi(*slojevi[i]);
				}
				break;
			}
			else {
				if (cnt == 0) {
					cnt++;
					expSloj = slojevi[i];
				}
				else {
					cnt++;
					expSloj->stopi(*slojevi[i]);
				}
			}
		}
	}
	expSloj->pretvori_u_unsignedchar();
	expSloj->eksportuj(directory);
}

void Slika::obrisiSelekciju(int i)
{
	if ((i < 0) || (i >= slojevi.size())) throw GreskaNeispravanIndeks();
	selekcije.erase(selekcije.begin() + i);
}

void Slika::obrisiSloj(int i)
{
	if ((i < 0) || (i >= slojevi.size())) throw GreskaNeispravanIndeks();
	slojevi.erase(slojevi.begin() + i);
}

bool Slika::aktivneSelekcije()
{
	if (any_of(selekcije.begin(), selekcije.end(), [](Selekcija* s)->bool {return s->getAktivan(); })) return true;
	return false;
}

void Slika::popuniBojom(int a)
{
	for (auto& i : slojevi) {
		if (i->getAktivan() == true) {

			for (auto& j : selekcije[a]->getPravougaonici()) {

				int poz = j->getY() * i->getWidth() + j->getX();
				for (int k = 0; k < j->getHeight() * j->getWidth(); k++) {

					if (((k % j->getWidth()) == 0) && (k != 0)) {
						poz += (i->getWidth() - j->getWidth());
					}

					i->getPiksel(poz)->setR(selekcije[a]->getR());
					i->getPiksel(poz)->setG(selekcije[a]->getG());
					i->getPiksel(poz)->setB(selekcije[a]->getB());
					i->getPiksel(poz)->setA(255);

					poz++;
				}
			}
		}
	}
}

Slika::Slika(const Slika& s)
{
	this->selekcije = s.selekcije;
	this->slojevi = s.slojevi;
}

Slika& Slika::operator=(const Slika& s)
{
	this->selekcije = s.selekcije;
	this->slojevi = s.slojevi;
	return *this;
}