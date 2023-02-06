package projekat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sloj {

    private Format format = null;

    private ArrayList<Pixel> pikseli = new ArrayList<Pixel>();
    private ArrayList<Integer> data = new ArrayList<Integer>();

    private int prozirnost = -1;
    private boolean aktivan = true;
    private boolean vidljiv = true;
    private boolean RGBA = true;
    private int width = 0;
    private int height = 0;
    private int size = 0;
    private String directory = "";


    public Sloj(String directory) throws Greska{

        this.directory = directory;
        Pattern rx1 = Pattern.compile("([^.]*).pam");
        Pattern rx2 = Pattern.compile("([^.]*).bmp");
        Matcher matcher1 = rx1.matcher(directory);
        Matcher matcher2 = rx2.matcher(directory);

        if (matcher1.matches()) {
            format = new PAMformat(directory);
            /*format.parse(pikseli);
            this.width = format.getWidth();
            this.height = format.getHeight();
            this.size = format.getSize();
            this.data = format.getData();
            if (((PAMformat) format).getTuplType() == "RGB_ALPHA") this.RGBA = true;
            else this.RGBA = false;
            if (this.RGBA == false) {
                this.RGBA = true;
                this.pretvori_u_unsignedchar();
            }
            ((PAMformat) (format)).setTuplTypeRGBA();*/

        } else if (matcher2.matches()) {
            format = new BMPformat(directory);
            try {
                BufferedImage img = ImageIO.read(new File(Meni.path + directory));
                this.width = img.getWidth();
                this.height = img.getHeight();
            } catch (IOException e) {

            }
            //format.parse(pikseli);
            /*this.width = format.getWidth();
            this.height = format.getHeight();
            this.size = format.getSize();
            this.data = format.getData();*/

        } else throw new Greska("Nedozvoljen format");

    }

    public Sloj(int w, int h) throws Greska{
        if ((w < 0) || (h < 0)) throw new Greska("Nevalidne dimenzije sloja");
        else {
            this.directory = "";
            this.width = w;
            this.height = h;
            this.size = 4 * width * height;
            this.RGBA = true;
            for (int i = 0; i < size; i += 4) {
                Pixel p = new Pixel(255, 255, 255, 0);
                pikseli.add(p);
            }
        }
    }

    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public int getSize() { return this.size; }

    public void aktiviraj() { this.aktivan = true; }
    public void deaktiviraj() { this.aktivan = false; }
    public boolean getAktivan() { return this.aktivan; }

    public void postaniVidljiv() { this.vidljiv = true; }
    public void postaniNevidljiv() { this.vidljiv = false; }
    public boolean getVidljiv() { return this.vidljiv; }

    public void setProzirnost(int a){
        try {
            if ((a < -1) || (a > 100)) throw new Greska("Nevalidna vrednost prozirnosti");
            else {
                this.prozirnost = a;
                postaviProzirnostNaPiksele();
            }
        }
        catch (Greska g) {}
    }
    public int getProzirnost() { return this.prozirnost;  }

    public ArrayList<Integer> getData() {
        this.pretvori_u_unsignedchar();
        return data;
    }

    public void setPikseli(ArrayList<Pixel> p){
        this.pikseli = p;
    }
    public ArrayList<Pixel> getPikseli(){
      return this.pikseli;
    }
    public Pixel getPiksel(int i){
        return pikseli.get(i);
    }

    public String getDirectory() { return this.directory; }
    void setDirectory(String d) { this.directory = d; }


    void postaviProzirnostNaPiksele(){
        double val = prozirnost / 100.0;
        if (val == 1.0) {
            for (Pixel p : pikseli) {
                p.setA(255);
            }
        }
        else {
            for (Pixel p : pikseli) {
                p.setA((int)(p.getA() * val));
            }
        }
    }

    void dopuni_sirinu(int val)
    {
        /*int d = (val - this.width) / 2;
        ArrayList<Pixel> noviPikseli = new ArrayList<>();
        int cnt = 0;
        for (int i = 0; i < (val * height); i++) {
            if (((i % val) < d) || ((i % val) >= (d + this.width))) {
                Pixel pik = new Pixel(255, 255, 255, 0);  // providan piksel
                noviPikseli.add(pik);
            }
		else {
                noviPikseli.add(pikseli.get(cnt++));
            }
        }
        pikseli.clear();
        for (int i = 0; i < noviPikseli.size(); i++) {
            pikseli.add(noviPikseli.get(i));
        }
        this.width = val;
        this.size = width * height * 4;*/
    }

    void dopuni_visinu(int val){
        /*int d = (val - this.width) / 2;
        ArrayList<Pixel> noviPikseli = new ArrayList<>();
        int cnt = 0;
        for (int i = 0; i < (val * width); i++) {
            if (((i / width) < d) || ((i / width) >= (d + this.height))) {
                Pixel pik = new Pixel(255, 255, 255, 0);  // providan piksel
                noviPikseli.add(pik);
            }
		else {
                noviPikseli.add(pikseli.get(cnt++));
            }
        }
        pikseli.clear();
        for (int i = 0; i < noviPikseli.size(); i++) {
            pikseli.add(noviPikseli.get(i));
        }
        this.height = val;
        this.size = width * height * 4;*/
    }

    void stopi(Sloj s){
        for (int i = 0; i < pikseli.size(); i++) {
            double a = ((double)pikseli.get(i).getA() / 255) + (1 - ((double)pikseli.get(i).getA() / 255)) * ((double)s.pikseli.get(i).getA() / 255);
            Pixel p = new Pixel((int)((pikseli.get(i).getR() * ((double)pikseli.get(i).getA() / 255 / a) + s.pikseli.get(i).getR() * (1 - (double)pikseli.get(i).getA() / 255) * ((double)s.pikseli.get(i).getA() / 255 / a))),
                    (int)(((pikseli.get(i).getG() * ((double)pikseli.get(i).getA() / 255 / a) + s.pikseli.get(i).getG() * (1 - (double)pikseli.get(i).getA() / 255) * ((double)s.pikseli.get(i).getA() / 255 / a)))),
                    (int)((pikseli.get(i).getB() * ((double)pikseli.get(i).getA() / 255 / a) + s.pikseli.get(i).getB() * (1 - (double)pikseli.get(i).getA() / 255) * ((double)s.pikseli.get(i).getA() / 255 / a))), (int)(a * 255));

            pikseli.remove(i);
            pikseli.add(i, p);      // TODO: PROVERI!!!!!

        }
    }

    void pretvori_u_unsignedchar(){
        data.clear();
        if (this.RGBA == true) {
            for (int i = 0; i < pikseli.size(); i++) {
                data.add(pikseli.get(i).getR());
                data.add(pikseli.get(i).getG());
                data.add(pikseli.get(i).getB());
                data.add(pikseli.get(i).getA());
            }
        }
	    else {
            for (int i = 0; i < pikseli.size(); i++) {
                data.add(pikseli.get(i).getR());
                data.add(pikseli.get(i).getG());
                data.add(pikseli.get(i).getB());
            }
        }
    }

    public void eksportuj(String izlaz){
        try {
            Pattern rx1 = Pattern.compile("([^.]*).pam");
            Pattern rx2 = Pattern.compile("([^.]*).bmp");
            Matcher matcher1 = rx1.matcher(izlaz);
            Matcher matcher2 = rx2.matcher(izlaz);
            if (matcher1.matches()) {
                format = new PAMformat(directory);
                ((PAMformat)(format)).setAll(this.width, this.height, this.size, this.data);
                (format).eksportuj(izlaz);
            } else if (matcher2.matches()) {
                format = new BMPformat(directory);
                ((BMPformat)(format)).postavi(this.width, this.height, this.pikseli);
                format.eksportuj(izlaz);
                ((BMPformat)(format)).reversePixels(this.pikseli);
            } else throw new Greska("Unet je nedozvoljen format");
        }
        catch (Greska greska){ }
    }

    public String pisi(){
        String s =/* "   " + width + "x" + height +*/ " prozirnost: ";
        if(prozirnost == -1) s+= "originalna  ";
        else s += prozirnost;
        if(aktivan) s += "  aktivan  ";
        else s += "  neaktivan  ";
        if(vidljiv) s+= "vidljiv";
        else s += "nevidljiv";
        return s;
    }
}
