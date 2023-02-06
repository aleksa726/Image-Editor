package projekat;

import java.util.ArrayList;

public abstract class Format {

    protected int width;
    protected int height;
    protected int size;
    protected ArrayList<Integer> data = new ArrayList<Integer>();
    protected int[] arr;
    protected String directory;

    public Format(String directory){
        this.directory = directory;
    }

    public abstract void parse(ArrayList<Pixel> pikseli);
    public abstract void eksportuj(String directory);

   public int getWidth() { return this.width; }
   public int getHeight() { return this.height; }
   public int getSize() { return this.size; }
   public ArrayList<Integer> getData() { return this.data; }

   public void setData(ArrayList<Integer> a) { data.clear(); data = a; }
   public void setDirectory(String dir) { directory = dir; }
   public void setSize(int s) { this.size = s; }

}
