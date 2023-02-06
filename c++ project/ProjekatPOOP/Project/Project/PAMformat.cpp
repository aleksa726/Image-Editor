#include "PAMformat.h"

void PAMformat::parse(vector<Pixel*>& pikseli)
{
	ifstream file(directory);
	if (file.is_open() == false) {
		throw GreskaOtvaranjeDatoteke();
	}
	else {
		string line;
		int cnt = 0;
		int size_info = 0;

		regex rx1("([A-Z]*) ([0-9]*)");
		regex rx2("([A-Z]*) ([A-Z]*)");

		while (getline(file, line) && cnt != 6) {
			switch (cnt) {
			case 0: {
				if (line != "P7") throw GreskaZapisDatoteke();
				break;
			}
			case 1: {
				smatch result;
				if (regex_match(line, result, rx1)) {
					width = atoi(result.str(2).c_str());
					itoa(width, widths, 10);
				}
				break;
			}
			case 2: {
				smatch result;
				if (regex_match(line, result, rx1)) {
					height = atoi(result.str(2).c_str());
					itoa(height, heights, 10);
				}
				break;
			}
			case 3: {
				smatch result;
				if (regex_match(line, result, rx1)) {
					depth = atoi(result.str(2).c_str());
				}
				break;
			}
			case 4: {
				smatch result;
				if (regex_match(line, result, rx1)) {
					maxval = atoi(result.str(2).c_str());
					itoa(maxval, maxvals, 10);
				}
				break;
			}
			case 5: {
				smatch result;
				if (regex_match(line, result, rx2)) {
					tupl_type = result.str(2);
				}
				else tupl_type = "RGB_ALPHA";
				break;
			}
			case 6: break;
			}

			cnt++;
		}


		if (tupl_type == "RGB_ALPHA") {
			size = 4 * width * height;
		}
		else size = 3 * width * height;

		/*for (int i = 0; i < size; i++) {
			getline(file, line);
			for (auto i : line) data.push_back(i);
			i += line.size();
			if(i != size - 1) data.push_back('\n');
			else {

			}

		}*/

		/*while (file.eof() != true) {
			getline(file, line);
			for (auto i : line) data.push_back(i);
			data.push_back('\n');
		}*/

		vector<char> pom;
		pom.resize(size);

		file.read(pom.data(), pom.size());
		data.assign(pom.begin(), pom.end());

		if (this->tupl_type == "RGB_ALPHA") {
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
			this->size = 4 * width * height;
		}
	}
}

void PAMformat::exportuj(string naziv)
{
	FILE* file = fopen(naziv.c_str(), "wb");
	if (file == nullptr) throw GreskaOtvaranjeDatoteke();

	fprintf(file, "P7\nWIDTH ");
	itoa(width, widths, 10);
	fprintf(file, widths);
	fprintf(file, "\n");
	fprintf(file, "HEIGHT ");
	itoa(height, heights, 10);
	fprintf(file, heights);
	fprintf(file, "\n");
	fprintf(file, "DEPTH ");
	fprintf(file, "4");
	fprintf(file, "\n");
	fprintf(file, "MAXVAL ");
	fprintf(file, "255");
	fprintf(file, "\n");
	fprintf(file, "TUPLTYPE ");
	fprintf(file, "RGB_ALPHA");
	fprintf(file, "\n");
	fprintf(file, "ENDHDR");
	fprintf(file, "\n");

	for (auto& i : data) {
		fputc(i, file);
	}

	fclose(file);
}

void PAMformat::setAll(int wid, int hei, int si, vector<unsigned char> d)
{
	this->setWidth(wid);
	this->setHeight(hei);
	this->setSize(si);
	this->setData(d);
}
