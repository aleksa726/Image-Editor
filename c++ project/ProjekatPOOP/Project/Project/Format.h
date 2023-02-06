#pragma once
#include <vector>
#include <string>
#include "Pixel.h"

using namespace std;

class GreskaOtvaranjeDatoteke {
public:
	GreskaOtvaranjeDatoteke() { cout << endl << endl << "   Error! Datoteka nije otvorena!" << endl; }
};

class GreskaZapisDatoteke {
public:
	GreskaZapisDatoteke() { cout << endl << endl << "   Error! Zapis datoteke nije ispravan!" << endl; }
};

class Format{

protected:
	int width;
	int height;
	int size;
	vector<unsigned char> data;
	string directory;
	
public:

	Format(string d) :directory(d) {};

	virtual void parse(vector<Pixel*>&) = 0;

	virtual void exportuj(string) = 0;

	int getWidth() const { return this->width; }
	int getHeight() const { return this->height; }
	int getSize() const { return this->size; }
	vector<unsigned char> getData() { return this->data; }

	void setData(vector<unsigned char> a) { data.clear(); data.assign(a.begin(), a.end()); }
	void setDirectory(string dir) { directory = dir; }
	void setSize(int s) { this->size = s; }
};

