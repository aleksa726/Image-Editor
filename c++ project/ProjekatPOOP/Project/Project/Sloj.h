#pragma once
#include "Pixel.h"
#include "PAMformat.h"
#include "BMPformat.h"
#include "Pixel.h"
#include <vector>
#include <string>
#include <iostream>

using namespace std;

class GreskaProzirnost {
public:
	GreskaProzirnost() { cout << endl << endl << "   Error! Neispravna vrednost je uneta za prozirnost!" << endl; }
};

class GreskaFormat {
public:
	GreskaFormat() { cout << endl << endl << "   Error! Neispravan format!" << endl; }
};

class GreskaDimenzijeSloja{
public:
	GreskaDimenzijeSloja() { cout << endl << endl << "   Error! Neispravne dimenzije sloja!" << endl; }
};

class GreskaNeispravanIndeks {
public:
	GreskaNeispravanIndeks() { cout << endl << endl << "   Error! Neispravan indeks!" << endl; }
};

class Pixel;
class XMLformat;

class Sloj{

	Format* format = nullptr;

	vector<Pixel*> pikseli;
	vector<unsigned char> data;

	int prozirnost = -1;
	bool aktivan = true;
	bool vidljiv = true;		
	bool RGBA = true;
	int width = 0;
	int height = 0;
	int size = 0;

	string directory;

public:

	Sloj() = default;
	Sloj(string directory);
	Sloj(int w, int h);

	void aktiviraj() { this->aktivan = true; }
	void deaktiviraj() { this->aktivan = false; }
	bool getAktivan() const { return this->aktivan; }

	void postaniVidljiv() { this->vidljiv = true; }
	void postaniNevidljiv() { this->vidljiv = false; }
	bool getVidljiv() const { return this->vidljiv; }

	void setProzirnost(int a);
	int getProzirnost() const { return this->prozirnost;  }

	int getWidth() const { return this->width; }
	int getHeight() const { return this->height; }

	int getSize() const { return this->size; }

	vector<unsigned char> getData() { this->pretvori_u_unsignedchar(); return this->data; }

	void setPikseli(vector<Pixel*>);
	vector<Pixel*> getPikseli() const;
	Pixel* getPiksel(int i);
	
	string getDirectory() const { return this->directory; }
	void setDirectory(string d) { this->directory = d; }

	void postaviProzirnostNaPiksele();

	void dopuni_sirinu(int val);
	void dopuni_visinu(int val);

	void stopi(Sloj& s);

	void pretvori_u_unsignedchar();

	void eksportuj(string izlaz);

	Sloj& operator =(const Sloj& s);

	friend ostream& operator << (ostream& out, const Sloj& s);
};

