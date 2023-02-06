#include "Pixel.h"

void Pixel::zaokruzi()
{
	if (R > 255) R = 255;
	if (R < 0) R = 0;
	if (G > 255) G = 255;
	if (G < 0) G = 0;
	if (B > 255) B = 255;
	if (B < 0) B = 0;
}
