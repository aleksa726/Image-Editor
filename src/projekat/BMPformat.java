package projekat;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BMPformat extends Format {

    private ArrayList<Character> data2 = new ArrayList<Character>();

    private BufferedImage img;


    public BMPformat(String directory) {
        super(directory);
    }

    @Override
    public void parse(ArrayList<Pixel> pikseli){

        /*FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(directory);
        } catch (FileNotFoundException e) {

        }
        Scanner scan = new Scanner(fileInputStream, StandardCharsets.ISO_8859_1);
        scan.useRadix(16);
        for(int i = 0; i < 10; i++){
            boolean b1 = scan.hasNext();
            String s = scan.next();
            for(int j = 0; j < s.length(); j++){
                char a = s.charAt(j);
                Character a1 = s.charAt(j);
                int i2 = a1.hashCode();

                System.out.println(a);
               int i1 = Integer.valueOf(String.valueOf(a));
            }
            boolean b2 = scan.hasNextByte();
            boolean b3 = scan.hasNextInt();
            System.out.println(1);
        }

*/
/*
        try {
            BufferedImage img = ImageIO.read(new File(directory));
            Raster raster = img.getData();
            DataBuffer db = raster.getDataBuffer();
            int i = db.getElem(0);
            int j = db.getElem(1);


        } catch (IOException e) {
            e.printStackTrace();
        }
 */
    }


    @Override
    public void eksportuj(String directory) {

    }


    public void reversePixels(ArrayList<Pixel> pikseli)
    {
        int cnt = 1;
        if (height % 2 == 0) {
            for (int i = 0; i < pikseli.size() / 2; i++) {
                Pixel tmp = pikseli.get(pikseli.size() - cnt * width + (i % width));
                pikseli.remove(pikseli.size() - cnt * width + (i % width));
                pikseli.add(pikseli.size() - cnt * width + (i % width),  pikseli.get(i));
                pikseli.remove(i);
                pikseli.add(i, tmp);
                if ((i + 1) % width == 0 && i != 0) cnt++;

            }
        }
        else {
            for (int i = 0; i < pikseli.size() / 2 - width / 2; i++) {
                Pixel tmp = pikseli.get(pikseli.size() - cnt * width + (i % width));
                pikseli.remove(pikseli.size() - cnt * width + (i % width));
                pikseli.add(pikseli.size() - cnt * width + (i % width),  pikseli.get(i));
                pikseli.remove(i);
                pikseli.add(i, tmp);
                if ((i + 1) % width == 0 && i != 0) cnt++;
            }
        }
    }

    public void postavi(int w, int h, ArrayList<Pixel> p){
        width = w;
        height = h;
        size = width * height * 4;
    }


}
