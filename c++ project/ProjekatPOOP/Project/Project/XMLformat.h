#pragma once
#include <string.h>
#include <iostream>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/xml_parser.hpp>
#include <boost/foreach.hpp>
#include <regex>
#include "Slika.h"
#include <exception>
#include "KompozitnaOperacija.h"

using namespace std;
using namespace boost;
using namespace boost::property_tree;

class XMLizuzetak : public boost::wrapexcept<boost::property_tree::ptree_bad_path> {
    virtual const char* what() const throw() {
        return "  Error! XML fajl nije ispravno napisan!";
    }
};

class KompozitnaOperacija;

class XMLformat {

    struct Sl {
        string directory;
    };

    struct Pravoug {
        int w, h, x, y;
    };

    struct Sel {
        string naziv;
        vector<Pravoug> pr;
    };

    struct Oper {
        string directory;
    };

    struct _Slika {
        vector<Sl> slojevi;
        vector<Sel> selekcije;
        vector<Oper> operacije;
    };

    const ptree& empty_ptree() {
        static ptree t;
        return t;
    }

public:

    XMLformat(string directory, Slika& sl, vector<KompozitnaOperacija*>& k);

    void postavi_sve(_Slika& s, Slika& glavna, vector<KompozitnaOperacija*>& k);

    static void eksportuj(string directory, Slika& sl, vector<KompozitnaOperacija*>& k);

};