package projekat;

import java.util.ArrayList;

public class Slika {

    private ArrayList<Sloj> slojevi = new ArrayList<Sloj>();
    private ArrayList<Selekcija> selekcije = new ArrayList<Selekcija>();

    private Meni meni;


    public Slika(Meni meni){ this.meni = meni; }

    public Sloj getSloj(int a) {
        try {
            if (this.slojevi.size() != 0) {
                if ((a < 0) || (a >= slojevi.size())) throw new Greska("Indeks je van opsega");
                return slojevi.get(a);
            } else return null;
        }
        catch (Greska g) {return null;}
    }


    public Selekcija getSelekcija(int a)
    {
        try {
            if ((a < 0) || (a >= selekcije.size())) throw new Greska("Indeks je van opsega");
            return selekcije.get(a);
        }
        catch (Greska g) { return null;}
    }


    public ArrayList<Sloj> getSlojeve()
    {
        return this.slojevi;
    }


    public ArrayList<Selekcija> getSel()
    {
        return this.selekcije;
    }


    public void dodajSloj(String directory)
    {
       try {
            Sloj sl = new Sloj(directory);

            if (this.getSloj(0) != null) {

                if (sl.getWidth() < slojevi.get(0).getWidth()) sl.dopuni_sirinu(slojevi.get(0).getWidth());
			else if (sl.getWidth() > slojevi.get(0).getWidth()) {
                    for (Sloj i : slojevi)i.dopuni_sirinu(sl.getWidth());
                }

                if (sl.getHeight() < slojevi.get(0).getHeight()) sl.dopuni_visinu(slojevi.get(0).getHeight());
			else if (sl.getHeight() > slojevi.get(0).getHeight()) {
                    for (Sloj i : slojevi)i.dopuni_visinu(sl.getHeight());
                }

            }
            slojevi.add(sl);
           this.meni.updateSlojevi();
       }
       catch (Greska gr) {}


    }


    public void dodajSloj(int w, int h) {
        try {
            Sloj sl = new Sloj(w, h);

            if (this.getSloj(0) != null) {

                if (sl.getWidth() < slojevi.get(0).getWidth()) sl.dopuni_sirinu(slojevi.get(0).getWidth());
                else if (sl.getWidth() > slojevi.get(0).getWidth()) {
                    for (Sloj i : slojevi) i.dopuni_sirinu(sl.getWidth());
                }

                if (sl.getHeight() < slojevi.get(0).getHeight()) sl.dopuni_visinu(slojevi.get(0).getHeight());
                else if (sl.getHeight() > slojevi.get(0).getHeight()) {
                    for (Sloj i : slojevi) i.dopuni_visinu(sl.getHeight());
                }

            }
            slojevi.add(sl);

            this.meni.updateSlojevi();
        }
        catch (Greska g){}
    }


    public void dodajSelekciju(Selekcija s)
    {
        this.selekcije.add(s);
    }


    public void exportuj(String directory)
    {
        int cnt = 0;
        Sloj expSloj = null;
        try {
            expSloj = new Sloj(slojevi.get(0).getWidth(), slojevi.get(0).getHeight());
        } catch (Greska greska) {}
        for (int i = slojevi.size() - 1; i >= 0; i--) {
            if (slojevi.get(i).getVidljiv() == true) {
                if (slojevi.get(i).getProzirnost() == 100) {
                    if (cnt == 0) {
                        cnt++;
                        expSloj = slojevi.get(i);
                    }
                    else {
                        cnt++;
                        expSloj.stopi(slojevi.get(i));
                    }
                    break;
                }
			    else {
                    if (cnt == 0) {
                        cnt++;
                        expSloj = slojevi.get(i);
                    }
                    else {
                        cnt++;
                        expSloj.stopi(slojevi.get(i));
                    }
                }
            }
        }
        expSloj.pretvori_u_unsignedchar();
        expSloj.eksportuj(directory);
    }

    public int getVidljivih(){
        int cnt = 0;
        for(var s:this.slojevi){
            if(s.getVidljiv() == true) cnt++;
        }
        return cnt;
    }


    public void obrisiSelekciju(int i) {
        try {
            if ((i < 0) || (i >= selekcije.size())) throw new Greska("Indeks je van opsega");
            selekcije.remove(i);
        }
        catch (Greska g) {}
    }


    public void obrisiSloj(int i) {
        try {
            if ((i < 0) || (i >= slojevi.size())) throw new Greska("Indeks je van opsega");
            slojevi.remove(i);
        }
        catch (Greska g) {}
    }


    public boolean aktivneSelekcije()
    {
        for(Selekcija s: selekcije){
            if(s.getAktivan() == true) return true;
        }
        return false;
    }


    public void popuniBojom(int a) {
        for (Sloj i : slojevi) {
            if (i.getAktivan() == true) {

                for (Pravougaonik j : selekcije.get(a).getPravougaonici()) {

                    int poz = j.getY() * i.getWidth() + j.getX();
                    for (int k = 0; k < j.getHeight() * j.getWidth(); k++) {

                        if (((k % j.getWidth()) == 0) && (k != 0)) {
                            poz += (i.getWidth() - j.getWidth());
                        }

                        i.getPiksel(poz).setR(selekcije.get(a).getR());
                        i.getPiksel(poz).setG(selekcije.get(a).getG());
                        i.getPiksel(poz).setB(selekcije.get(a).getB());
                        i.getPiksel(poz).setA(255);

                        poz++;
                    }
                }
            }
        }
    }

}
