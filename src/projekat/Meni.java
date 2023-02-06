package projekat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Meni extends JFrame {

    public static final String path = "C:\\Users\\Aleksa\\IdeaProjects\\projekatPOOPpokusaj2\\";
    public static final String exePath = "C:\\Users\\Aleksa\\Desktop\\Fakultet\\ProjekatPOOP\\Project\\Debug\\Project.exe ";

    private FrameSlike frejmSlike = new FrameSlike("Slika", instanca());

    private JPanel glavniPanel = new JPanel(new BorderLayout(10,10));
    private JPanel east = new JPanel();
    private JPanel center = new JPanel();
    private JPanel west = new JPanel();
    private JTabbedPane tabovi = new JTabbedPane();
    private JPanel slojevi = new JPanel(new GridBagLayout());
    private JPanel selekcije = new JPanel(new GridBagLayout());
   // private JPanel kompozitne = new JPanel(new GridBagLayout());

    private TextField val1 = new TextField();
    private TextField rgb1 = new TextField();

    private Font bold22 = new Font("Arial", Font.BOLD, 22);
    private Font bold16 = new Font("Arial", Font.BOLD, 16);
    private Font plain16 = new Font("Arial", Font.PLAIN, 16);

    private CheckboxGroup groupSlojevi;

    private Slika glavna = new Slika(this);

    public boolean kreiranjeSelekcije = false;
    private boolean napravljenaIzmena = true;

    public Set<Pravougaonik> pravougaonici = new HashSet<Pravougaonik>();
    private ArrayList<KompozitnaFunkcija> nizKompozitnih = new ArrayList<KompozitnaFunkcija>();
    private ArrayList<Operacija> operacije = new ArrayList<Operacija>();

    private boolean obradaSlojeva = false, obradaSelekcija = false, obradaOperacije = false, obradaKompozitnih = false, obradaEksporta = false;

    private Color bcg;

    public Meni() {
        super("Projekat");
        setBounds(new Rectangle(100, 50, 800, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        postaviEast();
        postaviCenter();
        postaviWest();

        add(glavniPanel);
        setVisible(true);
    }

    public Slika getGlavna() {
        return glavna;
    }

    public void postaviWest(){
        west = new JPanel();
        west.setLayout(new GridLayout(6,1));
        west.setBackground(Color.WHITE);

        JLabel lab = new JLabel("Meni");
        lab.setFont(bold22);
        lab.setHorizontalAlignment(SwingConstants.CENTER);
        west.add(lab);
        east.add(new JLabel("       \n        "));
        String[] str = {"Slojevi", "Selekcije", "Operacije", "Kompozitne funkcije", "Eksportovanje slike"};
        for(int i = 0; i < str.length; i++){
            JButton bt = new JButton(str[i]);
            bt.addActionListener(new OsluskivacMeniDugmica());
            west.add(bt);
        }

        glavniPanel.add(west, BorderLayout.WEST);
    }

    public void postaviCenter(){
        center.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        bcg = center.getBackground();

        JLabel lab = new JLabel("Unesite naziv slike", SwingConstants.CENTER);
        lab.setFont(bold16);

        gc.insets = new Insets(40,10,0,10);
        gc.gridx = 0;
        gc.gridy = 0;
        center.add(lab, gc);

        JTextField directory = new JTextField("", SwingConstants.CENTER);
        directory.setPreferredSize(new Dimension(280,25));

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(20,17,5,10);
        gc.weighty = 4;
        gc.gridx = 0;
        gc.gridy = 1;
        center.add(directory, gc);


        JButton ucitaj = new JButton("Ucitaj");
        ucitaj.setPreferredSize(new Dimension(120,50));
        ucitaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern rx1 = Pattern.compile("([^.]*).xml");
                Matcher matcher = rx1.matcher(directory.getText());
                if (matcher.matches()) {
                    XMLformat xml = new XMLformat("Data/" + directory.getText(), glavna);
                    xml.parse();
                    updateSelekcije();
                    updateSlojevi();
                    napravljenaIzmena = true;
                } else {
                    glavna.dodajSloj("Data/" + directory.getText());
                    napravljenaIzmena = true;
                }
            }
        });

        gc.insets = new Insets(0,10,50, 10);
        gc.anchor = GridBagConstraints.SOUTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 2;
        center.add(ucitaj, gc);


        glavniPanel.add(center, BorderLayout.CENTER);
    }

    public void postaviEast(){
        east.setPreferredSize(new Dimension(300,900));


        GridBagConstraints gc1 = new GridBagConstraints();

        slojevi.setLayout(new GridBagLayout());

        Border borderSlojevi = BorderFactory.createLineBorder(Color.BLACK, 1);
        slojevi.setBorder(borderSlojevi);
        slojevi.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gc2 = new GridBagConstraints();


        selekcije.setLayout(new GridBagLayout());

        Border borderSelekcije = BorderFactory.createLineBorder(Color.BLACK, 1);
        selekcije.setBorder(borderSelekcije);
        selekcije.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gc3 = new GridBagConstraints();


      /*  kompozitne.setLayout(new GridBagLayout());

        Border borderKompozitne = BorderFactory.createLineBorder(Color.BLACK, 1);
        kompozitne.setBorder(borderKompozitne);
        kompozitne.setBackground(Color.LIGHT_GRAY);*/


        tabovi.add("Slojevi", slojevi);
        tabovi.add("Selekcije", selekcije);
       // tabovi.add("Kompozitne operacije", kompozitne);
        tabovi.setPreferredSize(new Dimension(300,900));
        tabovi.setMinimumSize(new Dimension(280,890));
        east.add(tabovi);
        glavniPanel.add(east, BorderLayout.EAST);

    }


    private class OsluskivacMeniDugmica implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = ((JButton)e.getSource()).getText();
            switch(str){
                case "Slojevi":{
                    if(obradaSlojeva == false) {
                        obradaSlojeva = true;
                        obradaSelekcija = false;
                        obradaOperacije = false;
                        obradaKompozitnih = false;
                        obradaEksporta = false;
                        postaviPanelZaSlojeve();
                    }
                    break;
                }
                case "Selekcije":{
                    if(obradaSelekcija == false) {
                        obradaSlojeva = false;
                        obradaSelekcija = true;
                        obradaOperacije = false;
                        obradaKompozitnih = false;
                        obradaEksporta = false;
                        postaviPanelZaSelekcije();
                    }
                    break;
                }
                case "Operacije":{
                    if(obradaOperacije == false) {
                        obradaSlojeva = false;
                        obradaSelekcija = false;
                        obradaOperacije = true;
                        obradaKompozitnih = false;
                        obradaEksporta = false;
                        postaviPanelZaOperacije();
                    }
                    break;
                }
                case "Kompozitne funkcije":{
                    if(obradaKompozitnih == false) {
                        obradaSlojeva = false;
                        obradaSelekcija = false;
                        obradaOperacije = false;
                        obradaKompozitnih = true;
                        obradaEksporta = false;
                        postaviPanelZaKompozitneFunkcije();
                    }
                    break;

                }
                case "Eksportovanje slike":{
                    if(obradaEksporta == false) {
                        obradaSlojeva = false;
                        obradaSelekcija = false;
                        obradaOperacije = false;
                        obradaKompozitnih = false;
                        obradaEksporta = true;
                        postaviPanelZaEksportovanje();
                    }
                    break;
                }
            }
        }
    }


    public void postaviPanelZaSlojeve(){

        center.removeAll();
        center.revalidate();
        center.setBorder(BorderFactory.createEmptyBorder());
        GridBagConstraints gc = new GridBagConstraints();


        JLabel lab = new JLabel(" Novi sloj zadavanjem slike:");
        lab.setFont(bold16);
        gc.insets = new Insets(30,10,5,10);
        gc.weighty = 0;
        gc.gridx = 0;
        gc.gridy = 0;
        center.add(lab, gc);


        TextField directoryField = new TextField();
        directoryField.setPreferredSize(new Dimension(200,25));
        directoryField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                glavna.dodajSloj("Data/" + directoryField.getText());
                napravljenaIzmena = true;
            }
        });
        gc.insets = new Insets(0,10,30,10);
        gc.weighty = 3;
        gc.gridx = 0;
        gc.gridy = 1;
        center.add(directoryField, gc);


        lab = new  JLabel(" Novi sloj zadavanjem dimenzija:");
        gc.insets = new Insets(0,10,0,10);
        gc.weighty = 0;
        gc.gridx = 0;
        gc.gridy = 2;
        center.add(lab, gc);



        JPanel tmp = new JPanel(new FlowLayout());
        tmp.add(new JLabel("x: "));
        TextField xField = new TextField();
        xField.setPreferredSize(new Dimension(40,25));
        tmp.add(xField);
        tmp.add(new JLabel("    y: "));
        TextField yField = new TextField();
        yField.setPreferredSize(new Dimension(40,25));
        yField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    glavna.dodajSloj(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()));
                    Sloj s = glavna.getSloj(glavna.getSlojeve().size()-1);
                    BufferedImage img = new BufferedImage(s.getWidth(), s.getHeight(), BufferedImage.SCALE_DEFAULT);
                    int[] a = ( (DataBufferInt) img.getRaster().getDataBuffer() ).getData();
                    for(int i: a) i = 0;
                    boolean b = ImageIO.write(img,"BMP", new File(path + "Data/prazna" + String.valueOf(glavna.getSlojeve().size()-1) + ".bmp" ));
                    glavna.getSloj(glavna.getSlojeve().size()-1).setDirectory( "Data/prazna" + String.valueOf(glavna.getSlojeve().size()-1) + ".bmp" );
                    glavna.getSloj(glavna.getSlojeve().size()-1).setProzirnost(0);
                    KompozitnaFunkcija kompozitna = new KompozitnaFunkcija();
                    kompozitna.kreirajProzirnost(glavna, glavna.getSlojeve().size()-1, 0);

                    XMLformat xmLformat = new XMLformat(glavna);
                    xmLformat.kreiraj(glavna.getSloj(glavna.getSlojeve().size()-1).getDirectory());
                    napravljenaIzmena = true;

                    String file = exePath + "Data\\program.xml " + "Data\\operacija.xml";
                    Runtime runtime = Runtime.getRuntime();
                    try {
                        Process process = runtime.exec(file);
                        process.waitFor();
                    } catch (InterruptedException ex) {

                    } catch (IOException ex) {
                        new Greska("Fajl nije pronadjen!");
                    }
                    napravljenaIzmena = true;
                }
                catch (NumberFormatException | IOException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        tmp.add(yField);
        gc.insets = new Insets(0,0,40,0);
        gc.weighty = 5;
        gc.gridx = 0;
        gc.gridy = 3;
        center.add(tmp, gc);


        JPanel tmp1 = new JPanel(new FlowLayout());
        tmp1.add(new JLabel("Aktivirajte sloj "));
        TextField aktSloj = new TextField();
        aktSloj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    glavna.getSloj(Integer.parseInt(aktSloj.getText())).aktiviraj();
                    updateSlojevi();
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        aktSloj.setPreferredSize(new Dimension(25,25));
        tmp1.add(aktSloj);
        gc.insets = new Insets(0,10,0,0);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weighty = 6;
        gc.gridx = 0;
        gc.gridy = 4;
        center.add(tmp1, gc);

        JPanel tmp2 = new JPanel(new FlowLayout());
        tmp2.add(new JLabel("Deaktivirajte sloj "));
        TextField deaktSloj = new TextField();
        deaktSloj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    glavna.getSloj(Integer.parseInt(deaktSloj.getText())).deaktiviraj();
                    updateSlojevi();
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        deaktSloj.setPreferredSize(new Dimension(25,25));
        tmp2.add(deaktSloj);
        gc.gridx = 0;
        gc.gridy = 5;
        center.add(tmp2, gc);

        JPanel tmp3 = new JPanel(new FlowLayout());
        tmp3.add(new JLabel("Prozirnost sloja "));
        TextField prozirnost = new TextField();
        prozirnost.setPreferredSize(new Dimension(25,25));
        tmp3.add(prozirnost);
        tmp3.add(new JLabel("   val "));
        TextField valProzirnost = new TextField();
        valProzirnost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    KompozitnaFunkcija kompozitna = new KompozitnaFunkcija();
                    kompozitna.kreirajProzirnost(glavna, Integer.parseInt(prozirnost.getText()), Integer.parseInt(valProzirnost.getText()));

                    XMLformat xmLformat = new XMLformat(glavna);
                    xmLformat.kreiraj(glavna.getSloj(Integer.parseInt(prozirnost.getText())).getDirectory());
                    napravljenaIzmena = true;

                    String file = exePath + "Data\\program.xml " + "Data\\operacija.xml";
                    Runtime runtime = Runtime.getRuntime();
                    try {
                        Process process = runtime.exec(file);
                        process.waitFor();
                        frejmSlike.zatvori();
                        frejmSlike.ucitajSliku();
                    } catch (InterruptedException ex) {

                    } catch (IOException ex) {
                        new Greska("Fajl nije pronadjen!");
                    }

                    glavna.getSloj(Integer.parseInt(prozirnost.getText())).setProzirnost(Integer.parseInt(valProzirnost.getText()));
                    updateSlojevi();
                    napravljenaIzmena = true;
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        valProzirnost.setPreferredSize(new Dimension(40,25));
        tmp3.add(valProzirnost);
        gc.gridx = 0;
        gc.gridy = 6;
        center.add(tmp3, gc);

        JPanel tmp4 = new JPanel(new FlowLayout());
        tmp4.add(new JLabel("Sloj postaje vidljiv "));
        TextField vidljivSloj = new TextField();
        vidljivSloj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    glavna.getSloj(Integer.parseInt(vidljivSloj.getText())).postaniVidljiv();
                    updateSlojevi();
                    napravljenaIzmena = true;
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        vidljivSloj.setPreferredSize(new Dimension(25,25));
        tmp4.add(vidljivSloj);
        gc.gridx = 0;
        gc.gridy = 7;
        center.add(tmp4, gc);

        JPanel tmp5 = new JPanel(new FlowLayout());
        tmp5.add(new JLabel("Sloj postaje nevidljiv "));
        TextField nevidljivSloj = new TextField();
        nevidljivSloj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    glavna.getSloj(Integer.parseInt(nevidljivSloj.getText())).postaniNevidljiv();
                    updateSlojevi();
                    napravljenaIzmena = true;
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        nevidljivSloj.setPreferredSize(new Dimension(25,25));
        tmp5.add(nevidljivSloj);
        gc.gridx = 0;
        gc.gridy = 8;
        center.add(tmp5, gc);

        JPanel tmp6 = new JPanel(new FlowLayout());
        tmp6.add(new JLabel("Obrisi sloj "));
        TextField obrisiSloj = new TextField();
        obrisiSloj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    glavna.obrisiSloj(Integer.parseInt(obrisiSloj.getText()));
                    updateSlojevi();
                    napravljenaIzmena = true;
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        obrisiSloj.setPreferredSize(new Dimension(25,25));
        tmp6.add(obrisiSloj);
        gc.gridx = 0;
        gc.gridy = 9;
        center.add(tmp6, gc);

        center.revalidate();
        this.revalidate();
    }



    public void postaviPanelZaSelekcije(){

        center.removeAll();
        center.revalidate();
        center.setBorder(BorderFactory.createEmptyBorder());
        GridBagConstraints gc = new GridBagConstraints();


        JPanel tmp = new JPanel(new FlowLayout());
        JLabel nazivLab = new JLabel("Naziv selekcije     ");
        tmp.add(nazivLab);
        TextField naziv = new TextField();
        naziv.setPreferredSize(new Dimension(75,25));
        tmp.add(naziv, gc);
        gc.insets = new Insets(30,30,0,10);
        gc.gridx = 0;
        gc.gridy = 0;
        center.add(tmp, gc);

        JButton lab = new JButton("Kreiraj selekciju");
        lab.setPreferredSize(new Dimension(150,50));
        lab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(kreiranjeSelekcije == false){
                    lab.setText("Zavrsi kreiranje");
                    kreiranjeSelekcije = true;
                }
                else{
                    Set<Pravougaonik> pravougaoniciTMP = new HashSet<Pravougaonik>();
                    if(pravougaonici.size() > 0) {
                        for (Pravougaonik p : pravougaonici) {
                            Pravougaonik novi = new Pravougaonik(p.getWidth(), p.getHeight(), p.getX(), p.getY());
                            pravougaoniciTMP.add(novi);
                        }
                        Selekcija selekcija = new Selekcija(naziv.getText(), pravougaoniciTMP);
                        glavna.dodajSelekciju(selekcija);
                        updateSelekcije();
                        pravougaonici.clear();
                        frejmSlike.repaint();
                    }
                    lab.setText("Kreiraj selekciju");
                    kreiranjeSelekcije = false;
                }
            }
        });
        gc.insets = new Insets(30,30,60,10);
        gc.anchor = GridBagConstraints.CENTER;
        gc.weighty = 0;
        gc.gridx = 0;
        gc.gridy = 1;
        center.add(lab, gc);


        JPanel tmp1 = new JPanel(new FlowLayout());
        tmp1.add(new JLabel("Aktivirajte selekciju "));
        TextField aktSel = new TextField();
        aktSel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    glavna.getSelekcija(Integer.parseInt(aktSel.getText())).aktiviraj();
                    updateSelekcije();
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        tmp1.add(aktSel);
        gc.insets = new Insets(0,10,0,10);
        gc.anchor = GridBagConstraints.CENTER;
        gc.weighty = 5;
        gc.gridx = 0;
        gc.gridy = 2;
        center.add(tmp1, gc);

        JPanel tmp2 = new JPanel(new FlowLayout());
        tmp2.add(new JLabel("Deaktivirajte selekciju "));
        TextField deaktSel = new TextField();
        deaktSel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    glavna.getSelekcija(Integer.parseInt(deaktSel.getText())).deaktiviraj();
                    updateSelekcije();
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        tmp2.add(deaktSel);
        gc.gridx = 0;
        gc.gridy = 3;
        center.add(tmp2, gc);

        JPanel tmp3 = new JPanel(new FlowLayout());
        tmp3.add(new JLabel("Popuni selekciju zadatom bojom  "));
        JPanel tmp4 = new JPanel(new FlowLayout());
        tmp4.add(new JLabel("r "));
        TextField r = new TextField();
        tmp4.add(r);
        tmp4.add(new JLabel("g "));
        TextField g = new TextField();
        tmp4.add(g);
        tmp4.add(new JLabel("b "));
        TextField b = new TextField();
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    KompozitnaFunkcija kompozitna = new KompozitnaFunkcija();
                    kompozitna.kreirajPopuniBojom(glavna, "popunibojom", Integer.parseInt(r.getText()), Integer.parseInt(g.getText()), Integer.parseInt(b.getText()));
                    for (Sloj s : glavna.getSlojeve()) {
                        if (s.getAktivan() == true) {
                            XMLformat xmLformat = new XMLformat(glavna);
                            xmLformat.kreiraj(s.getDirectory());
                            napravljenaIzmena = true;

                            String file = exePath + "Data\\program.xml " + "Data\\operacija.xml";
                            Runtime runtime = Runtime.getRuntime();
                            try {
                                Process process = runtime.exec(file);
                                process.waitFor();
                                frejmSlike.zatvori();
                                frejmSlike.ucitajSliku();
                            } catch (InterruptedException ex) {

                            } catch (IOException ex) {
                                new Greska("Fajl nije pronadjen!");
                            }
                        }
                    }
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        tmp4.add(b);
        gc.insets = new Insets(30,10,-50,10);
        gc.weighty = 0;
        gc.gridx = 0;
        gc.gridy = 4;
        center.add(tmp3, gc);
        gc.insets = new Insets(0,10,0,10);
        gc.weighty = 5;
        gc.gridx = 0;
        gc.gridy = 5;
        center.add(tmp4, gc);


        JPanel tmp6 = new JPanel(new FlowLayout());
        tmp6.add(new JLabel("Obrisi selekciju"));
        TextField obrisiSel = new TextField();
        obrisiSel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    glavna.obrisiSelekciju(Integer.parseInt(obrisiSel.getText()));
                    updateSelekcije();
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        tmp6.add(obrisiSel);
        gc.gridx = 0;
        gc.gridy = 6;
        center.add(tmp6, gc);


        center.revalidate();
        this.revalidate();
    }


    public void postaviPanelZaOperacije(){

        center.removeAll();
        center.revalidate();
        center.setBorder(BorderFactory.createLineBorder(bcg, 1));
        GridBagConstraints gc = new GridBagConstraints();


        String[] str = {"Sabiranje", "Oduzimanje", "Inverzno oduzimanje", "Mnozenje", "Deljenje", "Inverzno deljenje",
                        "Power", "Log", "Abs", "Min", "Max", "Inverzija", "Nijansa sive", "Crno-belo", "Medijana"};

        gc.insets = new Insets(20,-100,0,5);
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 0;
        gc.weighty = 1;
        int cnt = 0;
        CheckboxGroup group = new CheckboxGroup();
        for(String s: str){
            Checkbox radio = new Checkbox(s, group, false);
            radio.addItemListener(new OsluskivacZaOperacije());
            gc.gridy = cnt;
            center.add(radio, gc);
            cnt++;
        }

        JPanel tmp = new JPanel(new FlowLayout());

        tmp.add(new JLabel("     rgb "));
        tmp.add(rgb1);


        tmp.add(new JLabel("               val "));
        tmp.add(val1);


        gc.insets = new Insets(30,-60,20,-10);
        gc.gridy = cnt++;
        center.add(tmp, gc);


        gc.anchor = GridBagConstraints.ABOVE_BASELINE;
        gc.insets = new Insets(0,0,20,0);
        JButton izvrsi = new JButton("Izvrsi");
        izvrsi.setPreferredSize(new Dimension(100,40));
        izvrsi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    KompozitnaFunkcija kompozitna = new KompozitnaFunkcija();
                    String naziv = group.getSelectedCheckbox().getLabel();
                    if (naziv == "Nijansa sive" || naziv == "Crno-belo" || naziv == "Medijana") {
                        if (naziv == "Nijansa sive") kompozitna.kreiraj(glavna, "nijansasive");
                        if (naziv == "Crno-belo") kompozitna.kreiraj(glavna, "crnobelo");
                        if (naziv == "Medijana") kompozitna.kreiraj(glavna, "medijana");
                    } else {
                        if (naziv == "Sabiranje")
                            kompozitna.kreiraj(glavna, "sabiranje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Oduzimanje")
                            kompozitna.kreiraj(glavna, "oduzimanje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Inverzno oduzimanje")
                            kompozitna.kreiraj(glavna, "inverznooduzimanje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Mnozenje")
                            kompozitna.kreiraj(glavna, "mnozenje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Deljenje")
                            kompozitna.kreiraj(glavna, "deljenje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Inverzno deljenje")
                            kompozitna.kreiraj(glavna, "inverznodeljenje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Power")
                            kompozitna.kreiraj(glavna, "power", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Log")
                            kompozitna.kreiraj(glavna, "log", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Min")
                            kompozitna.kreiraj(glavna, "min", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Max")
                            kompozitna.kreiraj(glavna, "max", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Abs") kompozitna.kreiraj(glavna, "abs", rgb1.getText());
                        if (naziv == "Inverzija") kompozitna.kreiraj(glavna, "inverzija", rgb1.getText());
                    }
                    napravljenaIzmena = true;

                    for (Sloj s : glavna.getSlojeve()) {
                        if (s.getAktivan() == true) {
                            XMLformat xmLformat = new XMLformat(glavna);
                            xmLformat.kreiraj(s.getDirectory());

                            String file = exePath + "Data\\program.xml " + "Data\\operacija.xml";
                            Runtime runtime = Runtime.getRuntime();
                            try {
                                Process process = runtime.exec(file);
                                process.waitFor();

                            } catch (InterruptedException ex) {

                            } catch (IOException ex) {
                                new Greska("Fajl nije pronadjen!");
                            }
                        }
                    }
                    frejmSlike.zatvori();
                    if(groupSlojevi.getSelectedCheckbox().getLabel().equals("Glavna")) {
                        groupSlojevi.setSelectedCheckbox(null);
                    }
                    else {
                        frejmSlike.ucitajSliku();
                    }
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        gc.gridy = cnt++;
        center.add(izvrsi, gc);

        center.revalidate();
        this.revalidate();
    }

    private class OsluskivacZaOperacije implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent e) {
            String naziv = ((Checkbox)e.getSource()).getLabel();
            if (naziv == "Nijansa sive" || naziv == "Crno-belo" || naziv == "Medijana" || naziv == "Inverzija" || naziv == "Abs") {
                val1.disable();
            }
            else{
                val1.enable();
            }
            if (naziv != "Nijansa sive" && naziv != "Crno-belo" && naziv != "Medijana") {
                rgb1.enable();
            }
            else{
                rgb1.disable();
            }
        }
    }

    public void postaviPanelZaEksportovanje(){
        center.removeAll();
        center.revalidate();
        center.setBorder(BorderFactory.createLineBorder(bcg, 1));
        GridBagConstraints gc = new GridBagConstraints();

        String[] str = {"PAM", "BMP", "XML"};

        gc.insets = new Insets(20,10,20,5);
        gc.anchor = GridBagConstraints.WEST;
        int cnt = 0;
        CheckboxGroup group = new CheckboxGroup();
        for(String s: str){
            Checkbox radio = new Checkbox(s, group, false);
            gc.gridy = cnt;
            center.add(radio, gc);
                   cnt++;
        }

        JPanel tmp = new JPanel(new FlowLayout());
        TextField directory = new TextField();
        directory.setPreferredSize(new Dimension(150,25));
        tmp.add(new JLabel("Naziv "));
        tmp.add(directory);

        gc.insets = new Insets(75,0,0,0);
        gc.gridy = cnt++;
        center.add(tmp, gc);

        JButton eksportuj = new JButton("Eksportuj");
        eksportuj.setPreferredSize(new Dimension(100,50));
        eksportuj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String ekst = "";
                if(group.getSelectedCheckbox().getLabel() == "PAM"){
                    ekst = ".pam";
                }
                if(group.getSelectedCheckbox().getLabel() == "BMP"){
                    ekst = ".bmp";
                }
                if(group.getSelectedCheckbox().getLabel() == "XML"){
                    ekst = ".xml";
                }

                if(glavna.getVidljivih() >= 1) {
                    if (ekst != ".xml") {

                        XMLformat xmLformat = new XMLformat(glavna);

                        xmLformat.trenutnoEksportovanje();

                        KompozitnaFunkcija kompozitnaFunkcija = new KompozitnaFunkcija();
                        kompozitnaFunkcija.kreirajEksp(path + "Data/" + directory.getText() + ekst);

                        String file = exePath + "Data\\exp.xml " + "Data\\oper.xml";
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            Process process = runtime.exec(file);
                            process.waitFor();
                            frejmSlike.zatvori();
                            frejmSlike.ucitajSliku();
                        } catch (InterruptedException ex) {

                        } catch (IOException ex) {
                            new Greska("Fajl nije pronadjen!");
                        }
                    } else {

                        XMLformat xmLformat = new XMLformat(glavna);
                        xmLformat.eksportuj(directory.getText() + ekst);
                    }
                }
            }
        });

        gc.insets = new Insets(300,50,0,0);
        gc.gridy = cnt++;
        center.add(eksportuj, gc);

        center.revalidate();
        this.revalidate();
    }

    public void postaviPanelZaKompozitneFunkcije(){
        center.removeAll();
        center.revalidate();
        center.setBorder(BorderFactory.createLineBorder(bcg, 1));
        GridBagConstraints gc = new GridBagConstraints();


        JLabel lab = new JLabel("Unesite naziv kompozitne funkcije", SwingConstants.CENTER);
        lab.setFont(bold16);

        gc.insets = new Insets(40,10,-60,10);
        gc.gridx = 0;
        gc.gridy = 0;
        center.add(lab, gc);


        JTextField directory = new JTextField(SwingConstants.CENTER);
        directory.setPreferredSize(new Dimension(250,25));

        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(-20,17,5,10);
        gc.weighty = 4;
        gc.gridx = 0;
        gc.gridy = 1;
        center.add(directory, gc);


        String[] str = {"Import", "Export"};
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = new Insets(20,10,20,5);
        int cnt = 2;
        CheckboxGroup group = new CheckboxGroup();
        for(String s: str){
            Checkbox radio = new Checkbox(s, group, false);
            gc.gridy = cnt;
            center.add(radio, gc);
            cnt++;
        }

        JButton ucitaj = new JButton("Izvrsi");
        ucitaj.setPreferredSize(new Dimension(120,50));
        ucitaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = group.getSelectedCheckbox().getLabel();
                if(str == "Import"){
                    if(glavna.getVidljivih() >= 1) {
                        KompozitnaFunkcija kompozitnaFunkcija = new KompozitnaFunkcija(path + "Data/" + directory.getText(), glavna);
                        kompozitnaFunkcija.parsiraj();
                        napravljenaIzmena = true;
                        frejmSlike.zatvori();
                        frejmSlike.ucitajSliku();
                    }
                }
                else if (str == "Export"){
                    postaviPanelZaEkportovanjeKompozitnijFunkcija(directory.getText());
                }
            }
        });

        gc.insets = new Insets(0,10,30, 10);
        gc.anchor = GridBagConstraints.SOUTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = cnt++;
        center.add(ucitaj, gc);

        center.revalidate();
        this.revalidate();
    }

    public void postaviPanelZaEkportovanjeKompozitnijFunkcija(String directory){
        center.removeAll();
        center.revalidate();
        center.setBorder(BorderFactory.createLineBorder(bcg, 1));
        GridBagConstraints gc = new GridBagConstraints();


        String[] str = {"Sabiranje", "Oduzimanje", "Inverzno oduzimanje", "Mnozenje", "Deljenje", "Inverzno deljenje",
                "Power", "Log", "Abs", "Min", "Max", "Inverzija", "Nijansa sive", "Crno-belo", "Medijana"};

        gc.insets = new Insets(20,-100,0,5);
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 0;
        gc.weighty = 1;
        int cnt = 0;
        CheckboxGroup group = new CheckboxGroup();
        for(String s: str){
            Checkbox radio = new Checkbox(s, group, false);
            radio.addItemListener(new OsluskivacZaOperacije());
            gc.gridy = cnt;
            center.add(radio, gc);
            cnt++;
        }

        JPanel tmp = new JPanel(new FlowLayout());

        tmp.add(new JLabel("     rgb "));
        tmp.add(rgb1);


        tmp.add(new JLabel("               val "));
        tmp.add(val1);


        gc.insets = new Insets(30,-60,20,-10);
        gc.gridy = cnt++;
        center.add(tmp, gc);


        //gc.anchor = GridBagConstraints.ABOVE_BASELINE;
        gc.insets = new Insets(0,0,20,0);
        JButton dodaj = new JButton("Dodaj");
        dodaj.setPreferredSize(new Dimension(100,40));
        dodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Operacija o = null;

                    String naziv = group.getSelectedCheckbox().getLabel();
                    if (naziv == "Nijansa sive" || naziv == "Crno-belo" || naziv == "Medijana") {
                        if (naziv == "Nijansa sive")  o = new Operacija("nijansasive");
                        if (naziv == "Crno-belo")  o = new Operacija("crnobelo");
                        if (naziv == "Medijana")  o = new Operacija("medijana");
                    } else {
                        if (naziv == "Sabiranje")
                            o = new Operacija("sabiranje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Oduzimanje")
                            o = new Operacija("oduzimanje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Inverzno oduzimanje")
                            o = new Operacija("inverznooduzimanje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Mnozenje")
                            o = new Operacija("mnozenje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Deljenje")
                            o = new Operacija("deljenje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Inverzno deljenje")
                            o = new Operacija("inverznodeljenje", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Power")
                            o = new Operacija("power", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Log")
                            o = new Operacija( "log", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Min")
                            o = new Operacija( "min", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Max")
                            o = new Operacija("max", rgb1.getText(), Integer.parseInt(val1.getText()));
                        if (naziv == "Abs") o = new Operacija( "abs", rgb1.getText());
                        if (naziv == "Inverzija")  o = new Operacija( "inverzija", rgb1.getText());
                    }
                    operacije.add(o);
                }
                catch (NumberFormatException g){
                    new Greska("Neispravno unet broj");
                }
            }
        });
        gc.gridy = cnt++;
        center.add(dodaj, gc);

        gc.insets = new Insets(0,0,20,0);
        JButton zavrsi = new JButton("Zavrsi");
        zavrsi.setPreferredSize(new Dimension(100,40));
        zavrsi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KompozitnaFunkcija kompozitna = new KompozitnaFunkcija();
                kompozitna.pisi(directory, operacije);
                operacije.clear();
                postaviPanelZaKompozitneFunkcije();
            }
        });
        gc.gridy = cnt++;
        center.add(zavrsi, gc);

        center.revalidate();
        this.revalidate();
    }


    public void updateSlojevi(){
        slojevi.removeAll();
        Border borderSlojevi = BorderFactory.createLineBorder(Color.BLACK, 1);
        slojevi.setBorder(borderSlojevi);
        slojevi.setBackground(Color.LIGHT_GRAY);
        slojevi.revalidate();
        east.revalidate();
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(30,34,50,10);
        groupSlojevi = new CheckboxGroup();

        Checkbox radio1 = new Checkbox("Glavna", groupSlojevi, false);
        radio1.addItemListener(new osluskivacSlojeva());
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        slojevi.add(radio1, gc);
        var lambdaContext = new Object() {
            int cnt = 1;
        };
        glavna.getSlojeve().forEach(sl -> {
            gc.gridx = 0;
            gc.gridy = lambdaContext.cnt;
            gc.anchor = GridBagConstraints.NORTH;
            gc.weighty = 1;
            gc.insets = new Insets(0,0,-100,0);
            Checkbox radio = new Checkbox(Integer.toString(lambdaContext.cnt - 1) + ".", groupSlojevi, false);
            radio.addItemListener(new osluskivacSlojeva());
            slojevi.add(radio, gc);

            gc.insets = new Insets(2,-30,0,0);
            gc.gridx = 1;
            slojevi.add(new JLabel(sl.pisi()), gc);
            lambdaContext.cnt++;
        });


        east.revalidate();
    }

    private Meni instanca(){
        return this;
    }

    private class osluskivacSlojeva implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent e) {
            String s = ((Checkbox)e.getSource()).getCheckboxGroup().getSelectedCheckbox().getLabel();
            if(s == "Glavna") {
                if(glavna.getVidljivih() >= 1) {
                    if (napravljenaIzmena == true) {
                        napravljenaIzmena = false;
                        XMLformat xmLformat = new XMLformat(glavna);
                        xmLformat.trenutnoEksportovanje();

                        KompozitnaFunkcija kompozitnaFunkcija = new KompozitnaFunkcija();
                        kompozitnaFunkcija.kreirajEksp(path + "Data/tmp.bmp");

                        String file = exePath + "Data\\exp.xml " + "Data\\oper.xml";
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            Process process = runtime.exec(file);
                            process.waitFor();
                        } catch (InterruptedException ex) {

                        } catch (IOException ex) {
                            new Greska("Fajl nije pronadjen!");
                        }
                    }
                    frejmSlike.setDirectory("Data/tmp.bmp");
                    frejmSlike.zatvori();
                    frejmSlike.ucitajSliku();
                }
                else{
                    frejmSlike.zatvori();
                }
            }
            else {
                char c = (s.charAt(0));
                int brojSloja = Integer.parseInt(String.valueOf(c));
                String putanjaDoSlike = glavna.getSloj(brojSloja).getDirectory();
                frejmSlike.setDirectory(putanjaDoSlike);
                frejmSlike.zatvori();
                frejmSlike.ucitajSliku();
            }

        }
    }

    public void updateSelekcije(){
        selekcije.removeAll();
        Border borderSlojevi = BorderFactory.createLineBorder(Color.BLACK, 1);
        selekcije.setBorder(borderSlojevi);
        selekcije.setBackground(Color.LIGHT_GRAY);
        selekcije.revalidate();
        east.revalidate();
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(30,10,20,10);
        var lambdaContext = new Object() {
            int cnt = 0;
        };
        glavna.getSel().forEach(sel -> {
            gc.gridx = 0;
            gc.gridy = lambdaContext.cnt;
            gc.anchor = GridBagConstraints.NORTH;
            gc.weighty = 1;
            gc.insets = new Insets(0,10,-100,10);
            JTextArea t = new JTextArea(lambdaContext.cnt + ".    " + sel.pisi());
            t.setBackground(Color.LIGHT_GRAY);
            t.setEditable(false);
            selekcije.add(t, gc);
            lambdaContext.cnt++;
        });

        east.revalidate();
    }

}
