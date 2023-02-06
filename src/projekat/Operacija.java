package projekat;

public class Operacija {
    private String naziv;
    private int val = -1;
    private String rgb = null;

    public Operacija(String naziv,String rgb, int val) {
        this.naziv = naziv;
        this.val = val;
        this.rgb = rgb;
    }

    public Operacija(String naziv) {
        this.naziv = naziv;
    }

    public Operacija(String naziv, String rgb) {
        this.naziv = naziv;
        this.rgb = rgb;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getVal() {
        return val;
    }

    public String getRgb() {
        return rgb;
    }
}
