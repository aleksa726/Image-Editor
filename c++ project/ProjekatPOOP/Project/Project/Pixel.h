#pragma once
#include <iostream>

using namespace std;

class Pixel{
	int R, G, B;
	int A;
public:
	Pixel(int r = 0, int g = 0, int b = 0, int a = 0) : R(r), G(g), B(b), A(a) {};

	friend ostream& operator << (ostream& out, const Pixel& p) { return out << p.R << " " << p.G << " " << p.B << " " << p.A; }

	int getR() { return this->R; }
	int getG() { return this->G; }
	int getB() { return this->B; }
	int getA() { return this->A; }

	void setR(int a) { this->R = a; }
	void setG(int a) { this->G = a; }
	void setB(int a) { this->B = a; }
	void setA(int a) { this->A = a; }

	void zaokruzi();
};

