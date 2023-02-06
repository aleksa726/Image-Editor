#include "Pravougaonik.h"

ostream& operator<<(ostream& out, const Pravougaonik& p)
{
	return out << "[" << p.width << ", " << p.height << ", (" << p.x << "," << p.y << ")]"; 
}
