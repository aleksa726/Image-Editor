package projekat;

import javax.swing.*;

public class Greska extends Exception{

    private String poruka;

    public Greska(String poruka){
        this.poruka = poruka;
        JOptionPane.showMessageDialog(null, poruka, "Upozorenje", JOptionPane.ERROR_MESSAGE);
    }
}
