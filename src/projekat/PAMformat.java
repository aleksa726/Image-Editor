package projekat;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PAMformat extends Format {

    private int depth = 0;
    private int maxval = 0;
    private String tupl_type = "";

    private String widths;
    private String heights;
    private String maxvals;


    public PAMformat(String directory) {
        super(directory);
    }

    @Override
    public void parse(ArrayList<Pixel> pikseli) {
        /*FileReader file = null;
        try {
            file = new FileReader(directory);
        } catch (FileNotFoundException e) {
           // throw GreskaOtvaranjeDatoteke();
        }
        BufferedReader bf = new BufferedReader(file);
*/

        String line;
        int size_info = 0;

        Pattern rx1 = Pattern.compile("([A-Z]*) ([0-9]*)");
        Pattern rx2 = Pattern.compile("([A-Z]*) ([A-Z]*)");


       // Stream<String> lines = bf.lines();

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(directory);
        } catch (FileNotFoundException e) {
            new Greska("Fajl nije pronadjen!");
        }
        Scanner scan = new Scanner(fileInputStream);


        int cnt = 0;

        while(cnt != 6) {
            line = scan.nextLine();
            Matcher matcher1 = rx1.matcher(line);
            Matcher matcher2 = rx2.matcher(line);

            if(cnt == 0){

            }
            if (matcher1.matches()) {
                switch (cnt) {
                    case 1: {
                        width = Integer.parseInt(matcher1.group(2));
                        widths = Integer.toString(width, 10);
                        break;
                    }
                    case 2: {
                        height = Integer.parseInt(matcher1.group(2));
                        heights = Integer.toString(height, 10);
                        break;
                    }
                    case 3: {
                        depth = Integer.parseInt(matcher1.group(2));
                        break;
                    }
                    case 4: {
                        maxval = Integer.parseInt(matcher1.group(2));
                        maxvals = Integer.toString(maxval, 10);
                        break;
                    }
                }
            } else {
                if (matcher2.matches()) {
                    tupl_type = matcher2.group(2);
                }
                else tupl_type = "RGB_ALPHA";
            }
            cnt++;
        }
        if (tupl_type == "RGB_ALPHA") {
            size = 4 * width * height;
        }
        else size = 3 * width * height;


        try {
            fileInputStream = new FileInputStream(directory);
        } catch (FileNotFoundException e) {

        }
        DataInputStream dis = new DataInputStream(fileInputStream);
        char c;
        int headerLines = 7;
        while(headerLines > 0) {
            do {
                try {
                    c = (char) (dis.readUnsignedByte());
                } catch (IOException e) {
                    c = ' ';
                }
            } while (c != '\n');
            headerLines--;
        }

        for(int i = 0; i < size; i++){
            try {
                int b = dis.readUnsignedByte();
                data.add(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        if (this.tupl_type == "RGB_ALPHA") {
            for (int i = 0; i < size; i += 4) {
                Pixel p = new Pixel(data.get(i), data.get(i+1), data.get(i+2), data.get(i+3));
                pikseli.add(p);
            }
        }
        else {
            for (int i = 0; i < size; i += 3) {
                Pixel p = new Pixel(data.get(i), data.get(i+1), data.get(i+2), 255);
                pikseli.add(p);
            }
            this.size = 4 * width * height;
        }

        try {
            fileInputStream.close();
        } catch (IOException e) {}

    }


    @Override
    public void eksportuj(String directory) {
        try {
            FileWriter fileWriter = new FileWriter("Data/" + directory, StandardCharsets.ISO_8859_1);
            fileWriter.write("P7\nWIDTH ");

            widths = Integer.toString(width, 10);
            fileWriter.write(widths);
            fileWriter.write("\n");
            fileWriter.write("HEIGHT ");
            heights = Integer.toString(height, 10);
            fileWriter.write(heights);
            fileWriter.write("\n");
            fileWriter.write("DEPTH ");
            fileWriter.write("4");
            fileWriter.write("\n");
            fileWriter.write("MAXVAL ");
            fileWriter.write("255");
            fileWriter.write("\n");
            fileWriter.write("TUPLTYPE ");
            fileWriter.write("RGB_ALPHA");
            fileWriter.write("\n");
            fileWriter.write("ENDHDR");
            fileWriter.write("\n");

            data.forEach(c->{
                try {
                    int i = c;
                    fileWriter.write(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            fileWriter.close();
        } catch (FileNotFoundException e) {
            new Greska("Fajl vec postoji!");
        } catch (IOException e) {

        }
    }

    public void setWidth(int w) { this.width = w; widths = Integer.toString(width,10);}
    public void setHeight(int h) { this.height = h; heights = Integer.toString(height,10);}
    public void setTuplTypeRGBA() { this.tupl_type = "RGB_ALPHA"; }
    public void setAll(int wid, int hei, int si, ArrayList<Integer> d){
        this.setWidth(wid);
        this.setHeight(hei);
        this.setSize(si);
        this.setData(d);
    }

    public String getTuplType()  { return this.tupl_type; }
}
