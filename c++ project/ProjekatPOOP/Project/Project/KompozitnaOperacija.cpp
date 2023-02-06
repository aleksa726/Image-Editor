#include "KompozitnaOperacija.h"

void KompozitnaOperacija::izvrsi(Slika& glavna)
{
	int r, g, b;
	ifstream file(directory);
	if (file.is_open() == false)throw GreskaOtvaranjeDatoteke();

	ptree tree;
	read_xml(directory, tree);

	Operacije operacije;
	BOOST_FOREACH(ptree::value_type const& v, tree.get_child("operacije")) {

		if (v.first == "operacija") {
			_Operacija o;
			o.naziv = v.second.get<std::string>("<xmlattr>.naziv");
			if (o.naziv == "popunibojom") {
				r = stoi(v.second.get<std::string>("<xmlattr>.r"));
				g = stoi(v.second.get<std::string>("<xmlattr>.g"));
				b = stoi(v.second.get<std::string>("<xmlattr>.b"));

			}
			else {
				if (o.naziv == "prozirnost") {
					int brojSloja = stoi(v.second.get<std::string>("<xmlattr>.sloj"));
					int val = stoi(v.second.get<std::string>("<xmlattr>.val"));
					glavna.getSloj(brojSloja)->setProzirnost(val);
				}
				else {
					if (o.naziv == "eksport") {
						string directory = (v.second.get<std::string>("<xmlattr>.directory"));
						glavna.exportuj(directory);
					}
					if ((o.naziv != "inverzija") && (o.naziv != "abs") && o.naziv != "nijansasive" && o.naziv != "crnobelo" && o.naziv != "medijana") {
						o.val = stoi(v.second.get<std::string>("<xmlattr>.val"));
					}
					if (o.naziv != "nijansasive" && o.naziv != "crnobelo" && o.naziv != "medijana") {
						o.rgb = v.second.get<std::string>("<xmlattr>.piksel");
					}
				}
			}
			operacije.operacije.push_back(o);
		}
	}

	for (auto& i : operacije.operacije) {
		if (i.naziv == "sabiranje") Operacija* o = new Sabiranje(glavna, i.val, i.rgb, true);
		if (i.naziv == "oduzimanje") Operacija* o = new Oduzimanje(glavna, i.val, i.rgb, true);
		if (i.naziv == "inverznooduzimanje") Operacija* o = new InverznoOduzimanje(glavna, i.val, i.rgb, true);
		if (i.naziv == "mnozenje") Operacija* o = new Mnozenje(glavna, i.val, i.rgb, true);
		if (i.naziv == "deljenje") Operacija* o = new Deljenje(glavna, i.val, i.rgb, true);
		if (i.naziv == "inverznodeljenje") Operacija* o = new InverznoDeljenje(glavna, i.val, i.rgb, true);
		if (i.naziv == "power") Operacija* o = new Power(glavna, i.val, i.rgb, true);
		if (i.naziv == "log") Operacija* o = new Logaritam(glavna, i.val, i.rgb, true);
		if (i.naziv == "abs") Operacija* o = new Absolute(glavna, i.rgb, true);
		if (i.naziv == "min") Operacija* o = new Min(glavna, i.val, i.rgb, true);
		if (i.naziv == "max") Operacija* o = new Max(glavna, i.val, i.rgb, true);
		if (i.naziv == "inverzija") Operacija* o = new Inverzija(glavna, i.rgb, true);
		if (i.naziv == "nijansasive") Operacija* o = new NijansaSive(glavna, true);
		if (i.naziv == "crnobelo") Operacija* o = new CrnoBelo(glavna, true);
		if (i.naziv == "medijana") Operacija* o = new Medijana(glavna, true);
		if (i.naziv == "popunibojom") {
			int cnt = 0;
			for (auto s : glavna.getSel()) {
				s->popuniBojom(r, g, b);
				glavna.popuniBojom(cnt);
				cnt++;
			}
		}
	}

	for (auto& i : glavna.getSlojeve()) {
		for (auto& j : i->getPikseli()) {
			j->zaokruzi();
		}
	}
}

void KompozitnaOperacija::pisi(string izlaz)
{
	ptree tree;

	cout << endl << endl << "   -Unesite \"kraj\" ukoliko ste zavrsili sa dodavanjem operacija" << endl << "   -Nazive upisivati malim slovima i spojeno" << endl << endl;
	while (1) {
		cout << endl << "   -Unesite naziv i parametre operacije koju dodajete: ";
		string naziv;
		int val;
		string rgb;
		cin >> naziv;
		if (naziv == "kraj") break;
		if ((naziv != "inverzija") && (naziv != "abs") && naziv != "nijansasive" && naziv != "crnobelo" && naziv != "medijana") cin >> val;
		if (naziv != "nijansasive" && naziv != "crnobelo" && naziv != "medijana") cin >> rgb;

		ptree* pt = new ptree();
		pt->put("<xmlattr>.naziv", naziv);
		if ((naziv != "inverzija") && (naziv != "abs") && naziv != "nijansasive" && naziv != "crnobelo" && naziv != "medijana") pt->put("<xmlattr>.val", val);
		if (naziv != "nijansasive" && naziv != "crnobelo" && naziv != "medijana") pt->put("<xmlattr>.piksel", rgb);
		tree.add_child("operacije.operacija", *pt);
	}

	write_xml(izlaz, tree);

	cout << endl << endl << "   -Uspesno se eksportovali kompozitnu operaciju!" << endl;
}
