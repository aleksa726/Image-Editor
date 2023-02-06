package projekat;

public class Pixel {

    private int R, G, B;
    private int A;

    public Pixel(int r, int g, int b, int a) {
        R = r;
        G = g;
        B = b;
        A = a;
    }

    public Pixel(){
        this(0,0,0,0);
    }

    public Pixel(int r, int g, int b){
        this(r,g,b,0);
    }

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }

    public void zaokruzi(){
        if (R > 255) R = 255;
        if (R < 0) R = 0;
        if (G > 255) G = 255;
        if (G < 0) G = 0;
        if (B > 255) B = 255;
        if (B < 0) B = 0;
    }

    @Override
    public String toString() {
        return "(" + R + ", " + G + ", " + B + ", " + A + ")";
    }
}
