#include "BMPformat.h"

void BMPformat::parse(vector<Pixel*>& pikseli)
{
	ifstream file(directory.c_str(), ios::binary);
	if (file.is_open() == false) throw GreskaOtvaranjeDatoteke();
	else {

		// read header
		file.read((char*)&id, sizeof(id));
		if (id != 19778) throw GreskaZapisDatoteke();
		file.read((char*)&file_size, sizeof(file_size));
		file.read((char*)&un1, sizeof(un1));
		file.read((char*)&un2, sizeof(un2));
		file.read((char*)&offset, sizeof(offset));

		// read DIB
		file.read((char*)&dib_size, sizeof(dib_size));
		file.read((char*)&width32, sizeof(width));
		width = (int)width32;
		file.read((char*)&height32, sizeof(height));
		height = (int)height32;
		file.read((char*)&plane1, sizeof(plane1));
		file.read((char*)&bit_per_pix, sizeof(bit_per_pix));
		file.read((char*)&bi_rgb, sizeof(bi_rgb));
		file.read((char*)&map_size, sizeof(map_size));
		file.read((char*)&fix1, sizeof(fix1));
		file.read((char*)&fix2, sizeof(fix2));
		file.read((char*)&fix3, sizeof(fix3));
		file.read((char*)&fix4, sizeof(fix4));

		if (bit_per_pix == 32) {
			// read color header
			file.read((char*)&red, sizeof(red));
			file.read((char*)&green, sizeof(green));
			file.read((char*)&blue, sizeof(blue));
			file.read((char*)&alpha, sizeof(alpha));
			file.read((char*)&LCS_WINDOWS_COLOR_SPACE, sizeof(LCS_WINDOWS_COLOR_SPACE));
			file.read((char*)&unused_LCS, sizeof(unused_LCS));
		}

		if (height < 0) cout << " VISINA MANJA OD 0!!!!" << endl;
		//starting in the lower left corner, going from left to right, and then row by row 
		//from the bottom to the top of the image. Unless BITMAPCOREHEADER is used, 
		//uncompressed Windows bitmaps also can be stored from the top to bottom, 
		//when the Image Height value is negative


		// read pixels
		if (bit_per_pix == 32) {
			size = 4 * height * width;
		}
		else size = 3 * height * width;
		file.seekg(offset);
		data2.resize(size);

		if ((bit_per_pix == 32) || (width % 4 == 0)) {
			file.read(data2.data(), data2.size());
		}
		else {
			// padding
			int a = width * bit_per_pix / 8;
			int pad = a;
			while (pad % 4 != 0) pad++;
			vector<uint8_t> padding(pad - a);
			for (int y = 0; y < height; y++) {
				file.read((char*)(data2.data() + a * y), a);
				file.read((char*)padding.data(), padding.size());
			}

		}
		data.assign(data2.begin(), data2.end());

		if (bit_per_pix == 32) {
			for (int i = 0; i < size; i += 4) {
				auto tmp = data[i];
				data[i] = data[i + 2];
				data[i + 2] = tmp;
			}
		}
		else {
			for (int i = 0; i < size; i += 3) {
				auto tmp = data[i];
				data[i] = data[i + 2];
				data[i + 2] = tmp;
			}
		}

		if (bit_per_pix == 32) {
			for (int i = 0; i < size; i += 4) {
				Pixel* p = new Pixel(data[i], data[i + 1], data[i + 2], data[i + 3]);
				pikseli.push_back(p);
			}
		}
		else {
			for (int i = 0; i < size; i += 3) {
				Pixel* p = new Pixel(data[i], data[i + 1], data[i + 2], 255);
				pikseli.push_back(p);
			}
		}

		reversePixels(pikseli);
		file.close();
	}
}

void BMPformat::exportuj(string directory)
{
	ofstream file(directory.c_str(), ios::binary);
	if (file.is_open() == false) throw GreskaOtvaranjeDatoteke();

	file.write((char*)&id, sizeof(id));
	file.write((char*)&file_size, sizeof(file_size));
	file.write((char*)&un1, sizeof(un1));
	file.write((char*)&un2, sizeof(un2));
	file.write((char*)&offset, sizeof(offset));

	file.write((char*)&dib_size, sizeof(dib_size));
	file.write((char*)&width32, sizeof(width));
	file.write((char*)&height32, sizeof(height));
	file.write((char*)&plane1, sizeof(plane1));
	file.write((char*)&bit_per_pix, sizeof(bit_per_pix));
	file.write((char*)&bi_rgb, sizeof(bi_rgb));
	file.write((char*)&map_size, sizeof(map_size));
	file.write((char*)&fix1, sizeof(fix1));
	file.write((char*)&fix2, sizeof(fix2));
	file.write((char*)&fix3, sizeof(fix3));
	file.write((char*)&fix4, sizeof(fix4));

	if (bit_per_pix == 32) {
		// write color header
		file.write((char*)&red, sizeof(red));
		file.write((char*)&green, sizeof(green));
		file.write((char*)&blue, sizeof(blue));
		file.write((char*)&alpha, sizeof(alpha));
		file.write((char*)&LCS_WINDOWS_COLOR_SPACE, sizeof(LCS_WINDOWS_COLOR_SPACE));
		file.write((char*)&unused_LCS, sizeof(unused_LCS));
	}

	// vrati data u stari polozaj
	for (int i = 0; i < size; i += 4) {
		auto tmp = data[i];
		data[i] = data[i + 2];
		data[i + 2] = tmp;
	}
	data2.resize(size);
	data2.assign(data.begin(), data.end());

	if ((bit_per_pix == 32) || (width % 4 == 0)) {
		file.write(data2.data(), data2.size());
	}
	else {
		// padding
		int a = width * bit_per_pix / 8;
		int pad = a;
		while (pad % 4 != 0) pad++;
		vector<uint8_t> padding(pad - a);
		for (int y = 0; y < height; y++) {
			file.write((char*)(data2.data() + a * y), a);
			file.write((char*)padding.data(), padding.size());
		}
	}

	file.close();
}

void BMPformat::reversePixels(vector<Pixel*>& pikseli)
{
	int cnt = 1;
	if (height % 2 == 0) {
		for (int i = 0; i < pikseli.size() / 2; i++) {
			auto tmp = pikseli[pikseli.size() - cnt * width + (i % width)];
			pikseli[pikseli.size() - cnt * width + (i % width)] = pikseli[i];
			pikseli[i] = tmp;
			if ((i + 1) % width == 0 && i != 0) cnt++;

		}
	}
	else {
		for (int i = 0; i < pikseli.size() / 2 - width / 2; i++) {
			auto tmp = pikseli[pikseli.size() - cnt * width + (i % width)];
			pikseli[pikseli.size() - cnt * width + (i % width)] = pikseli[i];
			pikseli[i] = tmp;
			if ((i + 1) % width == 0 && i != 0) cnt++;

		}
	}
}

void BMPformat::postavi(int w, int h, vector<Pixel*>& p)
{
	width = w;
	height = h;
	size = width * height * 4;

	id = (uint16_t)19778;
	offset = (uint32_t)(138);
	file_size = (uint32_t)(offset + width * height * 4);
	un1 = 0;
	un2 = 0;

	dib_size = (uint32_t)124;
	width32 = (uint32_t)w;
	height32 = (uint32_t)h;
	plane1 = (uint16_t)1;
	bit_per_pix = (uint16_t)32;
	bi_rgb = (uint32_t)3;
	map_size = (uint32_t)32;
	fix1 = (uint32_t)0;
	fix2 = (uint32_t)0;
	fix3 = (uint32_t)0;
	fix4 = (uint32_t)0;

	red = (uint32_t)16711680;
	green = (uint32_t)65280;
	blue = (uint32_t)255;
	alpha = (uint32_t)4278190080;
	LCS_WINDOWS_COLOR_SPACE = 1934772034;

	unused_LCS[0] = (uint32_t)687194752;
	unused_LCS[1] = (uint32_t)354334816;
	unused_LCS[2] = (uint32_t)32212256;
	unused_LCS[3] = (uint32_t)322122560;
	unused_LCS[4] = (uint32_t)644245120;
	unused_LCS[5] = (uint32_t)107374144;
	unused_LCS[6] = (uint32_t)161061280;
	unused_LCS[7] = (uint32_t)64424508;
	unused_LCS[8] = (uint32_t)848256036;
	unused_LCS[9] = (uint32_t)0;
	unused_LCS[10] = (uint32_t)0;
	unused_LCS[11] = (uint32_t)0;
	unused_LCS[12] = (uint32_t)4;
	unused_LCS[13] = (uint32_t)0;
	unused_LCS[14] = (uint32_t)0;
	unused_LCS[15] = (uint32_t)0;

	reversePixels(p);
	int cnt = 0;
	data.resize(size);
	for (auto& i : p) {
		data[cnt++] = i->getR();
		data[cnt++] = i->getG();
		data[cnt++] = i->getB();
		data[cnt++] = i->getA();
	}
}
