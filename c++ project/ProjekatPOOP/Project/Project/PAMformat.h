#pragma once
#include <fstream>
#include <iostream>
#include <string>
#include <regex>
#include <vector>
#include <iterator>
#include "Pixel.h"
#include "Format.h"

using namespace std;

class Pixel;

class PAMformat: public Format {

	int depth = 0;
	int maxval = 0;
	string tupl_type = "";

	char widths[100];
	char heights[100];
	char maxvals[100];
	
public:

	PAMformat() = default;

	PAMformat(string dir) :Format(dir) {};

	void parse(vector<Pixel*>& pikseli);

	void exportuj(string naziv);

	void setWidth(int w) { this->width = w; itoa(width, widths, 10);}
	void setHeight(int h) { this->height = h; itoa(height, heights, 10);}
	void setTuplTypeRGBA() { this->tupl_type = "RGB_ALPHA"; }
	void setAll(int wid, int hei, int si, vector<unsigned char> d);

	string getTuplType() const { return this->tupl_type; }

};

