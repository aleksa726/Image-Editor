#pragma once
#include <iostream>
#include <string>
#include <regex>
#include <algorithm>
#include <vector>
#include "PAMformat.h"
#include "BMPformat.h"
#include "Sloj.h"
#include "Selekcija.h"
#include "Format.h"

using namespace std;

class Sloj;
class Selekcija;
class GreskaNeispravanIndeks;

class Slika{

	vector<Sloj*> slojevi;
	vector<Selekcija*> selekcije;

public:

	Slika() = default;
	Slika(const Slika& s);

	Sloj* getSloj(int a);		
	Selekcija& getSelekcija(int a);

	vector<Sloj*> getSlojeve() const;
	vector<Selekcija*> getSel() const;

	void dodajSloj(string directory);
	void dodajSloj(int w, int h);

	void dodajSelekciju(Selekcija* s);

	void pisi_slojeve();
	void pisi_selekcije();
	
	void exportuj(string directory);

	void obrisiSelekciju(int i);
	void obrisiSloj(int i);

	bool aktivneSelekcije();

	void popuniBojom(int a);

	Slika& operator =(const Slika& s);


};

