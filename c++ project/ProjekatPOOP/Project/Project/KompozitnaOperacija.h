#pragma once
#include "Operacija.h"
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/xml_parser.hpp>
#include <boost/foreach.hpp>
#include <vector>

#include "Sabiranje.h"
#include "Oduzimanje.h"
#include "InverznoOduzimanje.h"
#include "Mnozenje.h"
#include "Deljenje.h"
#include "InverznoDeljenje.h"
#include "Power.h"
#include "Min.h"
#include "Max.h"
#include "Inverzija.h"
#include "CrnoBelo.h"
#include "NijansaSive.h"
#include "Medijana.h"
#include "Logaritam.h"
#include "Absolute.h"

using namespace std;
using namespace boost;
using namespace boost::property_tree;

class KompozitnaOperacija: public Operacija{

	string directory;
	
	struct _Operacija {
		string naziv;
		int val;
		string rgb;
	};

	struct Operacije {
		vector<_Operacija> operacije;
	};

public:

	KompozitnaOperacija(Slika& s, string dir) : directory(dir) { izvrsi(s); }

	void izvrsi(Pixel& p) {};

	void izvrsi(Slika& glavna);

	static void pisi(string izlaz);

	string getDirectory() const { return directory; }
};