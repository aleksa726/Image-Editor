#include <iostream>
#include <fstream>
#include <iterator>
#include <string>
#include <set>

#include "PAMformat.h"
#include "BMPformat.h"
#include "XMLformat.h"
#include "Slika.h"
#include "Pravougaonik.h"
#include "Selekcija.h"
#include "Sabiranje.h"
#include "Pixel.h"
#include "Operacija.h"
#include "Format.h"

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
#include "KompozitnaOperacija.h"

using namespace std;

int main(int argc, char* argv[]) {

	Slika glavna;
	static vector<KompozitnaOperacija*> kompozitne;

	if (argc != 1) {
		try {
			regex rx1("([^.]*).pam");
			regex rx2("([^.]*).bmp");
			regex rx3("([^.]*).xml");
			smatch result;
			string directory = argv[1];
			string kompDirectory = argv[2];

			if (regex_match(directory, result, rx1)) {
				glavna.dodajSloj(directory);
			}
			else if (regex_match(directory, result, rx2)) {
				glavna.dodajSloj(directory);
			}
			else if (regex_match(directory, result, rx3)) {
				XMLformat* xml = new XMLformat(directory, glavna, kompozitne);
			}

			if (regex_match(kompDirectory, result, rx3)) {
				KompozitnaOperacija* k = new KompozitnaOperacija(glavna, kompDirectory);
			}
			glavna.exportuj(glavna.getSloj(0)->getDirectory());
		}
		catch (GreskaOtvaranjeDatoteke& gr) {}
		catch (GreskaZapisDatoteke& gr) {}
		catch (XMLizuzetak& gr) {}
		catch (boost::wrapexcept<boost::property_tree::ptree_bad_path>& gr) {
			cout << endl << "  Error! XML fajl nije ispravno napisan!" << endl;
		}

		exit(0);
	}
	else {

		cout << "Meni: " << endl;
		cout << endl << "  1. Ucitavanje slike" << endl;
		cout << "  2. Rad sa slojevima" << endl;
		cout << "  3. Rad sa selekcijama" << endl;
		cout << "  4. Operacije nad slikom" << endl;
		cout << "  5. Operacije sa kompozitnim funkcijama" << endl;
		cout << "  6. Eksportovanje slike" << endl;
		cout << "  0. Kraj programa" << endl;

		cout << endl << endl << "Izaberite jednu od stavki: ";
		int w;
		cin >> w;

		bool nijeExportovano = true;
		
		while (1) {
			switch (w) {
			case 1: {
				try {
					string directory;
					cout << endl << " -Unesite naziv datoteke: ";
					cin >> directory;

					regex rx("([^.]*).xml");
					smatch result;
					if (regex_match(directory, result, rx)) {
						XMLformat* xml = new XMLformat(directory, glavna, kompozitne);
					}
					else {
						glavna.dodajSloj(directory);
					}
				}
				catch (GreskaOtvaranjeDatoteke& gr) {}
				catch (GreskaZapisDatoteke& gr) {}
				catch (XMLizuzetak& gr) {}
				catch (boost::wrapexcept<boost::property_tree::ptree_bad_path>& gr) {
					cout << endl << "  Error! XML fajl nije ispravno napisan!" << endl;
				}
				break;
			}
			case 2: {

				cout << endl;
				cout << "   1. Kreirajte sloj zadavanjem slike" << endl;
				cout << "   2. Kreirajte sloj zadavanjem dimenzija" << endl;
				cout << "   3. Aktivirajte zadati sloj" << endl;
				cout << "   4. Deaktivirajte zadati sloj" << endl;
				cout << "   5. Postavi prozirnost" << endl;
				cout << "   6. Zadati sloj postaje vidljiv" << endl;
				cout << "   7. Zadati sloj postaje nevidljiv" << endl;
				cout << "   8. Ispisi sve slojeve" << endl;
				cout << "   9. Obrisi sloj" << endl;
				cout << "   0. Vratite se nazad" << endl;

				int z;
				cout << endl << "   Izaberite jednu od stavki: ";
				cin >> z;

				while (z != 0) {
					try {
						switch (z) {
						case 1: {
							cout << endl << "   -Unesite naziv slike: ";
							string naziv;
							cin >> naziv;
							glavna.dodajSloj(naziv);
							cout << endl << "   -Sloj je uspesno kreiran!" << endl;
							break;
						}
						case 2: {
							glavna.dodajSloj(0,0);
							cout << endl << "   -Sloj je uspesno kreiran!" << endl;
							break;
						}
						case 3: {
							cout << endl << "   -Unesite redni broj sloja koji zelite da aktivirate: ";
							int i;
							cin >> i;
							glavna.getSloj(i)->aktiviraj();
							break;
						}
						case 4: {
							cout << endl << "   -Unesite redni broj sloja koji zelite da deaktivirate: ";
							int i;
							cin >> i;
							glavna.getSloj(i)->deaktiviraj();
							break;
						}
						case 5: {
							cout << endl << "   -Unesite redni broj sloja kom zelite da postavite prozirnost: ";
							int i;
							cin >> i;
							cout << endl << "   -Unesite vredonst prozirnosti za " << i << ". sloj: ";
							int val;
							cin >> val;
							glavna.getSloj(i)->setProzirnost(val);
							break;
						}
						case 6: {
							cout << endl << "   -Unesite redni broj sloja koji zelite da postane vidljiv: ";
							int i;
							cin >> i;
							glavna.getSloj(i)->postaniVidljiv();
							break;
						}
						case 7: {
							cout << endl << "   -Unesite redni broj sloja koji zelite da postane nevidljiv: ";
							int i;
							cin >> i;
							glavna.getSloj(i)->postaniNevidljiv();
						}
						case 8: {
							cout << endl;
							glavna.pisi_slojeve();
							break;
						}
						case 9: {
							cout << endl << "   -Unesite redni broj sloja koji zelite da obrisete: ";
							int i;
							cin >> i;
							glavna.obrisiSloj(i);
							cout << endl << "   -Sloj je uspesno obrisan!" << endl;
							break;
						}
						default: cout << endl << "   -Izabrali ste nepostojecu stavku!!!" << endl; break;

						}
					}
					catch (GreskaProzirnost& gr) {}
					catch (GreskaFormat& gr) {}
					catch (GreskaDimenzijeSloja& gr) {}
					catch (GreskaNeispravanIndeks& gr) {}
					catch (GreskaOtvaranjeDatoteke& gr) {}
					catch (GreskaZapisDatoteke& gr) {}
					cout << endl << endl << "   Izaberite jednu od stavki: ";
					cin >> z;
				}
				cout << endl << endl;
				cout << "Meni: " << endl;
				cout << endl << "  1. Ucitavanje slike" << endl;
				cout << "  2. Rad sa slojevima" << endl;
				cout << "  3. Rad sa selekcijama" << endl;
				cout << "  4. Operacije nad slikom" << endl;
				cout << "  5. Operacije sa kompozitnim funkcijama" << endl;
				cout << "  6. Eksportovanje slike" << endl;
				cout << "  0. Kraj programa" << endl;
				break;
			}
			case 3: {

				cout << endl << "   1. Kreirajte selekciju" << endl;
				cout << "   2. Aktivirajte zadatu selekciju" << endl;
				cout << "   3. Deaktivirajte zadatu selekciju" << endl;
				cout << "   4. Popunite selekciju zadatom bojom" << endl;
				cout << "   5. Ispisi sve selekcije" << endl;
				cout << "   6. Obrisi selekciju" << endl;
				cout << "   0. Vratite se nazad" << endl;

				int z;
				cout << endl << "   Izaberite jednu od stavki: ";
				cin >> z;

				while (z != 0) {
					try {
						switch (z) {
						case 1: {
							int n;
							cout << endl << "   -Unesite broj pravougaonika koji dodajete u selekciju: ";
							cin >> n;
							cout << endl;
							set<Pravougaonik*> p;
							for (int i = 0; i < n; i++) {
								int _w, h, x, y;
								cout << "     -Unesite parametre " << i + 1 << ". pravougaonika (sirina, visina, x i y ose gornjeg levog ugla): ";
								cin >> _w >> h >> x >> y;

								while (_w <= 0 || h <= 0 || x < 0 || y < 0) {
									cout << endl << "   -Unete dimenzije pravougaonika nisu validne!" << endl << endl;
									cout << "     -Unesite parametre " << i + 1 << ". pravougaonika (sirina, visina, x i y ose gornjeg levog ugla): ";
									cin >> _w >> h >> x >> y;
								}
								while ((_w > (glavna.getSloj(0)->getWidth() - x)) || (h > (glavna.getSloj(0)->getHeight() - y))) {
									cout << endl << "   -Unete dimenzije pravougaonika nisu validne!" << endl << endl;
									cout << "     -Unesite parametre " << i + 1 << ". pravougaonika (sirina, visina, x i y ose gornjeg levog ugla): ";
									cin >> _w >> h >> x >> y;
								}

								Pravougaonik* pravougaonik = new Pravougaonik(_w, h, x, y);
								p.insert(pravougaonik);



							}
							cout << endl << "   -Unesite naziv selekcije: ";
							string naziv;
							cin >> naziv;
							Selekcija* sel = new Selekcija(naziv, p);
							glavna.dodajSelekciju(sel);
							break;
						}
						case 2: {
							cout << endl << "   -Unesite redni broj selekcije koju zelite da aktivirate: ";
							int i;
							cin >> i;
							glavna.getSelekcija(i).aktiviraj();
							break;
						}
						case 3: {
							cout << endl << "   -Unesite redni broj selekcije koju zelite da deaktivirate: ";
							int i;
							cin >> i;
							glavna.getSelekcija(i).deaktiviraj();
							break;
						}
						case 4: {
							cout << endl << "   -Unesite redni broj selekcije koju zelite da popunite zadatom bojom: ";
							int i;
							cin >> i;
							cout << endl << "   -Unesite boju (R, G, B): ";
							int r, g, b;
							cin >> r >> g >> b;
							glavna.getSelekcija(i).popuniBojom(r, g, b);
							glavna.popuniBojom(i);
							break;
						}
						case 5: {
							glavna.pisi_selekcije();
							break;
						}
						case 6: {
							cout << endl << "   -Unesite redni broj selekcije koju zelite da obrisete: ";
							int i;
							cin >> i;
							glavna.obrisiSelekciju(i);
							cout << endl << "   -Selekcija je uspesno obrisana!" << endl;
							break;
						}
						default: cout << endl << "   -Izabrali ste nepostojecu stavku!!!" << endl; break;
						}
					}
					catch (GreskaNeispravanIndeks& gr) {}
					catch (GreskaVrednostPiksela& gr) {}
					catch (GreskaOtvaranjeDatoteke& gr) {}
					catch (GreskaZapisDatoteke& gr) {}

					cout << endl << endl << "   Izaberite jednu od stavki: ";
					cin >> z;
				}
				cout << endl << endl;
				cout << "Meni: " << endl;
				cout << endl << "  1. Ucitavanje slike" << endl;
				cout << "  2. Rad sa slojevima" << endl;
				cout << "  3. Rad sa selekcijama" << endl;
				cout << "  4. Operacije nad slikom" << endl;
				cout << "  5. Operacije sa kompozitnim funkcijama" << endl;
				cout << "  6. Eksportovanje slike" << endl;
				cout << "  0. Kraj programa" << endl;
				break;
			}
			case 4: {
				cout << endl << "   1.  Sabiranje" << endl;
				cout << "   2.  Oduzimanje" << endl;
				cout << "   3.  Inverzno oduzimanje" << endl;
				cout << "   4.  Mnozenje" << endl;
				cout << "   5.  Deljenje" << endl;
				cout << "   6.  Inverzno deljenje" << endl;
				cout << "   7.  Power" << endl;
				cout << "   8.  Log" << endl;
				cout << "   9.  Abs" << endl;
				cout << "   10. Min" << endl;
				cout << "   11. Max" << endl;
				cout << "   12. Inverzija" << endl;
				cout << "   13. Nijansa sive" << endl;
				cout << "   14. Crno-belo" << endl;
				cout << "   15. Medijana" << endl;
				cout << "    0. Vratite se nazad" << endl;

				int z;
				cout << endl << "   Izaberite jednu od stavki: ";
				cin >> z;

				while (z != 0) {
					try {
						switch (z) {

						case 1: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Sabiranje(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 2: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Oduzimanje(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 3: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new InverznoOduzimanje(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 4: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Mnozenje(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 5: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Deljenje(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 6: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new InverznoDeljenje(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 7: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Power(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 8: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Logaritam(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 9: {
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Absolute(glavna, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 10: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Min(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 11: {
							int val;
							cout << endl << "   Unesite parametar: ";
							cin >> val;
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Max(glavna, val, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 12: {
							string rgb;
							cout << endl << "   Unesite na koju se boju odnosi operacija (r, rg, rgb...): ";
							cin >> rgb;

							Operacija* o = new Inverzija(glavna, rgb, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 13: {

							Operacija* o = new NijansaSive(glavna, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 14: {

							Operacija* o = new CrnoBelo(glavna, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						case 15: {

							Operacija* o = new Medijana(glavna, false);

							cout << endl << "   -Operacija je uspesno izvrsena!" << endl;
							break;
						}
						default: cout << endl << "   -Izabrali ste nepostojecu stavku!!!" << endl; break;

						}
					}
					catch (Greska_RGB_Operacija& greska) {};

					nijeExportovano = true;
					cout << endl << endl << "   Izaberite jednu od stavki: ";
					cin >> z;
				}

				cout << endl << endl;
				cout << "Meni: " << endl;
				cout << endl << "  1. Ucitavanje slike" << endl;
				cout << "  2. Rad sa slojevima" << endl;
				cout << "  3. Rad sa selekcijama" << endl;
				cout << "  4. Operacije nad slikom" << endl;
				cout << "  5. Operacije sa kompozitnim funkcijama" << endl;
				cout << "  6. Eksportovanje slike" << endl;
				cout << "  0. Kraj programa" << endl;
				break;
			}
			case 5: {

				cout << endl << "   1.  Citaj kompozitnu operaciju" << endl;
				cout << "   2.  Pisi kompozitnu operaciju" << endl;
				cout << "   0.  Vratite se nazad" << endl;

				int z;
				cout << endl << "   Izaberite jednu od stavki: ";
				cin >> z;

				while (z != 0) {
					try {
						switch (z) {
						case 1: {

							string naziv;
							cout << endl << "   -Unesite naziv kompozitne operacije (sa ekstenzijom): ";
							cin >> naziv;
							regex rx("([^.]*).xml");
							smatch result;
							if (regex_match(naziv, result, rx)) {
								KompozitnaOperacija* komp = new KompozitnaOperacija(glavna, naziv);
								cout << endl << "   -Kompozitna operacija je uspesno izvrsena!" << endl;
							}
							else {
								cout << endl << "   -Uneta je nepravila ekstenzija!" << endl;
							}

							break;
						}

						case 2: {

							string naziv;
							cout << endl << "   -Unesite naziv kompozitne operacije (sa ekstenzijom): ";
							cin >> naziv;
							regex rx("([^.]*).xml");
							smatch result;
							if (regex_match(naziv, result, rx)) {
								KompozitnaOperacija::pisi(naziv);
							}
							else {
								cout << endl << "   -Uneta je nepravila ekstenzija!" << endl;
							}
							break;
						}
						default: cout << endl << "   -Izabrali ste nepostojecu stavku!!!" << endl; break;
						}
					}
					catch (GreskaOtvaranjeDatoteke& gr) {}

					cout << endl << endl << "   Izaberite jednu od stavki: ";
					cin >> z;
				}
				break;
			}

			case 6: {
				try {
					string naziv;
					cout << endl << " -Unesite naziv izlazne datoteke (sa ekstenzijom): ";
					cin >> naziv;

					regex rx("([^.]*).xml");
					smatch result;
					if (regex_match(naziv, result, rx)) {
						XMLformat::eksportuj(naziv, glavna, kompozitne);
					}
					else glavna.exportuj(naziv);
					nijeExportovano = false;
					cout << endl;
				}
				catch (GreskaFormat& gr) {}
				catch (GreskaOtvaranjeDatoteke& gr) {}
				catch (GreskaZapisDatoteke& gr) {}
				catch (boost::wrapexcept<boost::property_tree::ptree_bad_path>& gr) {
					cout << endl << "  Error! XML fajl nije ispravno napisan!" << endl;
				}
				break;
			}
			case 0: {
				cout << endl << " -Da li ste sigurni da zelite da zavrsite sa radom?" << endl;
				if (nijeExportovano == true) {
					cout << endl << " UPOZORENJE! Niste generisali izlazni fajl nakon poslednje izmene" << endl;
				}
				cout << endl << " (y/n):";
				string odgovor;
				cin >> odgovor;
				if (odgovor != "y" && odgovor != "n") {
					cout << endl << " -Uneli ste pogresnu vrednost!" << endl;
				}
				if (odgovor == "y") {
					cout << endl << " -Uspesno ste zavrsili sa radom programa!" << endl;
					exit(0);
				}
				break;
			}
			default: {
				cout << endl << " -Izabrali ste nepostojecu stavku!!!" << endl;
				break;
			}
			}
			cout << endl << endl << "Izaberite jednu od stavki: ";
			cin >> w;
		}
	}
}