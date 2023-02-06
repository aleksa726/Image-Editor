#include "XMLformat.h"
#include "KompozitnaOperacija.h"


XMLformat::XMLformat(string directory, Slika& sl, vector<KompozitnaOperacija*>& k)
{

    ifstream file(directory);
    if (file.is_open() == false)throw GreskaOtvaranjeDatoteke();

    ptree tree;
    read_xml(directory, tree);

    _Slika slika;

    BOOST_FOREACH(ptree::value_type const& v, tree.get_child("slika")) {

        if (v.first == "sloj") {
            Sl s;
            s.directory = v.second.get<std::string>("<xmlattr>.directory");
            slika.slojevi.push_back(s);
        }

        if (v.first == "selekcija") {
            Sel s;
            s.naziv = v.second.get<std::string>("<xmlattr>.naziv");

            const ptree& attributes = v.second.get_child("pravougaonik", empty_ptree());
            BOOST_FOREACH(ptree::value_type const& it, attributes) {
                Pravoug p;
                p.w = stoi(it.second.get<string>("width"));
                p.h = stoi(it.second.get<string>("height"));
                p.x = stoi(it.second.get<string>("x"));
                p.y = stoi(it.second.get<string>("y"));

                s.pr.push_back(p);
            }

            slika.selekcije.push_back(s);
        }

        if (v.first == "kompozitna") {
            Oper o;
            o.directory = v.second.get<std::string>("<xmlattr>.directory");
            
            slika.operacije.push_back(o);
        }
    }

    postavi_sve(slika, sl, k);

}


void XMLformat::eksportuj(string directory, Slika& glavna, vector<KompozitnaOperacija*>& k){
    ptree tree;

    for (auto& i : glavna.getSlojeve()) {
        ptree* pt = new ptree();
        pt->put("<xmlattr>.directory", i->getDirectory());
        tree.add_child("slika.sloj", *pt);
    }

    for (auto& i : glavna.getSel()) {
        ptree* pt = new ptree();
        pt->put("<xmlattr>.naziv", i->getIme());
        for (auto& it : i->getPravougaonici()) {
            ptree* pt2 = new ptree();
            pt2->put("<xmlattr>.width", it->getWidth());
            pt2->put("<xmlattr>.height", it->getHeight());
            pt2->put("<xmlattr>.x", it->getX());
            pt2->put("<xmlattr>.y", it->getY());
            pt->add_child("pravougaonik", *pt2);
        }
        tree.add_child("slika.selekcija", *pt);
    }

    for (auto& i : k) {
        ptree* pt = new ptree();
        pt->put("<xmlattr>.directory", i->getDirectory());
        tree.add_child("slika.kompozitna", *pt);
    }

    write_xml(directory, tree);
}


void XMLformat::postavi_sve(_Slika& s, Slika& glavna, vector<KompozitnaOperacija*>& komp) {
    for (auto& i : s.slojevi) {
        glavna.dodajSloj(i.directory);
    }

    for (auto& i : s.selekcije) {
        set<Pravougaonik*> p;
        for (auto& it : i.pr) {
            Pravougaonik* pravoug = new Pravougaonik(it.w, it.h, it.x, it.y);
            p.insert(pravoug);
        }
        Selekcija* sel = new Selekcija(i.naziv, p);
        glavna.dodajSelekciju(sel);
    }

    for (auto& i : s.operacije) {
        KompozitnaOperacija* k = new KompozitnaOperacija(glavna, i.directory);
        komp.push_back(k);
    }

}

