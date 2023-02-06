package projekat;

public class Pravougaonik {

   private int width, height;
   private int x, y;

    public Pravougaonik(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String pisi(){
        String s =  "[" + width + ", " + height + ", (" + x + "," + y + ")]";
        return s;
    }

}
