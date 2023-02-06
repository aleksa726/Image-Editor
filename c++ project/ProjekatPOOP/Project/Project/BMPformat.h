#pragma once
#include <vector>
#include <fstream>
#include <iostream>
#include <string>
#include <regex>
#include "Format.h"
#include "Pixel.h"

using namespace std;

class Pixel;

class BMPformat: public Format{

	vector<char> data2;

	//header
	uint16_t  id;
	uint32_t  file_size;
	uint16_t  un1;
	uint16_t  un2;
	uint32_t  offset;
	
	//DIB
	uint32_t  dib_size;
	uint32_t width32;
	uint32_t height32;
	uint16_t  plane1;
	uint16_t  bit_per_pix;
	uint32_t  bi_rgb; 
	uint32_t  map_size; 
	uint32_t fix1;
	uint32_t fix2;
	uint32_t fix3;
	uint32_t fix4;

	//color header
	uint32_t  red;
	uint32_t  green;
	uint32_t  blue;
	uint32_t  alpha;
	uint32_t  LCS_WINDOWS_COLOR_SPACE;
	uint32_t  unused_LCS[16];

public:

	BMPformat() = default;
	BMPformat(string dir) : Format(dir) {};

	void parse(vector<Pixel*>& pikseli);
	void exportuj(string directory);
	void reversePixels(vector<Pixel*>& pikseli);
	void postavi(int w, int h, vector<Pixel*>& p);

	void setHeight(int a) { height = a; }
	void setWidth(int a) { width = a; }
};

