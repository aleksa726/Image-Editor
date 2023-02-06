#pragma once
#include <iostream>

using namespace std;



class Pravougaonik{

	int width, height;
	int x, y;

public:

	Pravougaonik(int _w, int _h, int _x, int _y) : width(_w), height(_h), x(_x), y(_y) {};

	friend ostream& operator<<(ostream& out, const Pravougaonik& p);

	int getWidth() const { return this->width; }
	int getHeight() const { return this->height; }
	int getX() const { return this->x; }
	int getY() const { return this->y; }

};

