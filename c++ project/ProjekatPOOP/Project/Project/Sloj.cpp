#include "Sloj.h"

Sloj::Sloj(string directory)
{
	this->directory = directory;

	regex rx1("([^.]*).pam");
	regex rx2("([^.]*).bmp");
	smatch result;
	
	if (regex_match(directory, result, rx1)) {
		format = new PAMformat(directory);
		format->parse(pikseli);
		this->width = format->getWidth();
		this->height = format->getHeight();
		this->size = format->getSize();
		this->data = format->getData();

		if (static_cast<PAMformat*>(format)->getTuplType() == "RGB_ALPHA") this->RGBA = true;
		else this->RGBA = false;

		if (this->RGBA == false) {
			this->RGBA = true;
			this->pretvori_u_unsignedchar();
		}
		static_cast<PAMformat*>(format)->setTuplTypeRGBA();

	}
	else if (regex_match(directory, result, rx2)) {

		format = new BMPformat(directory);
		format->parse(pikseli);
		this->width = format->getWidth();
		this->height = format->getHeight();
		this->size = format->getSize();
		this->data = format->getData();

	}
	else throw GreskaFormat();
}

Sloj::Sloj(int w, int h)
{
	if ((w < 0) || (h < 0)) throw GreskaDimenzijeSloja();
	else {
		this->width = w;
		this->height = h;
		this->size = 4 * width * height;
		this->RGBA = true;
		for (int i = 0; i < size; i += 4) {
			Pixel* p = new Pixel(255, 255, 255, 0);
			pikseli.push_back(p);
		}
	}
}

void Sloj::setProzirnost(int a)
{
	if ((a < -1) || (a > 100)) throw GreskaProzirnost();
	else {
		this->prozirnost = a;
		postaviProzirnostNaPiksele();
	}
}

void Sloj::setPikseli(vector<Pixel*> p)
{
	this->pikseli.assign(p.begin(), p.end());
}

vector<Pixel*> Sloj::getPikseli() const
{
	return this->pikseli;
}

Pixel* Sloj::getPiksel(int i)
{
	return pikseli[i];
}

void Sloj::postaviProzirnostNaPiksele()
{
	double val = prozirnost / 100.0;
	if (val == 1.0) {
		for (auto& p : pikseli) {
			p->setA(255);
		}
	}
	else {
		for (auto& p : pikseli) {
			p->setA(p->getA() * val);
		}
	}
}

void Sloj::dopuni_sirinu(int val)
{
	int d = (val - this->width) / 2;
	vector<Pixel*> noviPikseli;
	int cnt = 0;
	for (int i = 0; i < (val * height); i++) {
		if (((i % val) < d) || ((i % val) >= (d + this->width))) {
			Pixel* pik = new Pixel(255, 255, 255, 0);  // providan piksel
			noviPikseli.push_back(pik);
		}
		else {
			noviPikseli.push_back(pikseli[cnt++]);
		}
	}
	pikseli.clear();
	for (int i = 0; i < noviPikseli.size(); i++) {
		pikseli.push_back(noviPikseli[i]);
	}
	this->width = val;
	this->size = width * height * 4;
}

void Sloj::dopuni_visinu(int val)
{
	int d = (val - this->height) / 2;
	vector<Pixel*> noviPikseli;
	int cnt = 0;
	for (int i = 0; i < (val * width); i++) {
		if (((i / width) < d) || ((i / width) >= (d + this->height))) {
			Pixel* pik = new Pixel(255, 255, 255, 0);  // providan piksel
			noviPikseli.push_back(pik);
		}
		else {
			noviPikseli.push_back(pikseli[cnt++]);
		}
	}
	pikseli.clear();
	for (int i = 0; i < noviPikseli.size(); i++) {
		pikseli.push_back(noviPikseli[i]);
	}
	this->height = val;
	this->size = width * height * 4;
}

void Sloj::stopi(Sloj& s)
{
	for (int i = 0; i < pikseli.size(); i++) {
		double a = ((double)pikseli[i]->getA() / 255) + (1 - ((double)pikseli[i]->getA() / 255)) * ((double)s.pikseli[i]->getA() / 255);
		Pixel* p = new Pixel((pikseli[i]->getR() * ((double)pikseli[i]->getA() / 255 / a) + s.pikseli[i]->getR() * (1 - (double)pikseli[i]->getA() / 255) * ((double)s.pikseli[i]->getA() / 255 / a)),
			(pikseli[i]->getG() * ((double)pikseli[i]->getA() / 255 / a) + s.pikseli[i]->getG() * (1 - (double)pikseli[i]->getA() / 255) * ((double)s.pikseli[i]->getA() / 255 / a)),
			(pikseli[i]->getB() * ((double)pikseli[i]->getA() / 255 / a) + s.pikseli[i]->getB() * (1 - (double)pikseli[i]->getA() / 255) * ((double)s.pikseli[i]->getA() / 255 / a)), a * 255);
		pikseli[i] = p;
		if (i == 200) {
			cout << " ";
		}
	}
}

void Sloj::pretvori_u_unsignedchar()
{

	data.clear();

	if (this->RGBA == true) {
		for (int i = 0; i < pikseli.size(); i++) {
			data.push_back(pikseli[i]->getR());
			data.push_back(pikseli[i]->getG());
			data.push_back(pikseli[i]->getB());
			data.push_back(pikseli[i]->getA());
		}
	}
	else {
		for (int i = 0; i < pikseli.size(); i++) {
			data.push_back(pikseli[i]->getR());
			data.push_back(pikseli[i]->getG());
			data.push_back(pikseli[i]->getB());
		}
	}
}

Sloj& Sloj::operator=(const Sloj& s)
{
	this->directory = s.directory;
	this->format = s.format;
	this->width = s.width;
	this->height = s.height;
	this->size = s.size;
	this->aktivan = s.aktivan;
	this->vidljiv = s.vidljiv;
	this->prozirnost = s.prozirnost;
	this->pikseli.assign(s.pikseli.begin(), s.pikseli.end());
	this->data.assign(s.data.begin(), s.data.end());
	return *this;
}

void Sloj::eksportuj(string izlaz)
{
	regex rx1("([^.]*).pam");
	regex rx2("([^.]*).bmp");
	smatch result;
	if (regex_match(izlaz, result, rx1)) {
		format = new PAMformat(directory);
		static_cast<PAMformat*>(format)->setAll(this->width, this->height, this->size, this->data);
		format->exportuj(izlaz);
		cout << endl << " -Slika je uspesno eksportovana!";
	}
	else if (regex_match(izlaz, result, rx2)) {
		format = new BMPformat(directory);
		static_cast<BMPformat*>(format)->postavi(this->width, this->height, this->pikseli);
		format->exportuj(izlaz);
		static_cast<BMPformat*>(format)->reversePixels(this->pikseli);
		cout << endl << " -Slika je uspesno eksportovana!";
	}
	else throw GreskaFormat();
}

ostream& operator<<(ostream& out, const Sloj& s)
{
	out << "sirina: " << s.width << " visina: " << s.height << " prozirnost: " << s.prozirnost;
	if (s.aktivan) out << " aktivan";
	else out << " neaktivan";
	if (s.vidljiv) out << " vidljiv";
	else out << " nevidljiv";
	return out;
}
