package projekat;

import java.util.HashSet;
import java.util.Set;

public class Selekcija {

    private Set<Pravougaonik> pravougaonici = new HashSet<Pravougaonik>();
    private String ime;
    private boolean aktivna = true;
    private int R = -1, G = -1, B = -1;


    Selekcija(String i, Set<Pravougaonik> pr){
        this.ime = i;
        this.pravougaonici = pr;
    }

    void popuniBojom(int r, int g, int b) {
        try {
            if ((r < 0) || (r > 255) || (g < 0) || (g > 255) || (b < 0) || (b > 255))
                throw new Greska("Nedozvoljena boja");
            this.R = r;
            this.G = g;
            this.B = b;
        }
        catch (Greska gr) {}
    }

    public Set<Pravougaonik> getPravougaonici(){
        return this.pravougaonici;
    }

    public void aktiviraj() { this.aktivna = true; }
    public void deaktiviraj() { this.aktivna = false; }
    public boolean getAktivan() { return this.aktivna; }
    public String getIme() { return this.ime; }
    public int getR() { return R; }
    public int getG() { return G; }
    public int getB() { return B; }

    public String pisi(){
        String s = ime + "  ";
        if(aktivna) s += "aktivna";
        else s += "neaktivna";
        for(Pravougaonik p: pravougaonici){
            s += "\n";
            s += p.pisi();
        }
        return s;
    }

}
