package projekat;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class XMLformat{

    String path = "C:\\Users\\Aleksa\\IdeaProjects\\projekatPOOPpokusaj2\\";

    private class Sl{
        private String directory;

        public Sl(String directory) {
            this.directory = directory;
        }
    }

    private class Pravoug{
        private int w,h,x,y;

        public Pravoug(int w, int h, int x, int y) {
            this.w = w;
            this.h = h;
            this.x = x;
            this.y = y;
        }
    }

    private class Sel{
        private String naziv;
        private ArrayList<Pravoug> pr = new ArrayList<Pravoug>();

        public Sel(String naziv, ArrayList<Pravoug> pr) {
            this.naziv = naziv;
            this.pr = pr;
        }
    }

    private class Oper{
        private String directory;

        public Oper(String directory) {
            this.directory = directory;
        }
    }

    private class _Slika{
       public ArrayList<Sl> slojevi = new ArrayList<Sl>();
       public ArrayList<Sel> selekcije = new ArrayList<Sel>();
       public ArrayList<Oper> operacije = new ArrayList<Oper>();

        public void setSlojevi(ArrayList<Sl> slojevi) {
            this.slojevi = slojevi;
        }

        public void setSelekcije(ArrayList<Sel> selekcije) {
            this.selekcije = selekcije;
        }

        public void setOperacije(ArrayList<Oper> operacije) {
            this.operacije = operacije;
        }
    }


    private String directory = null;
    private Slika glavna = null;
    public XMLformat(String directory, Slika glavna) {
        this.directory = directory;
        this.glavna = glavna;
    }
    public XMLformat(Slika glavna){
        this.glavna = glavna;
    }


    public void parse() {

        _Slika slika = new _Slika();

        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(directory);

            NodeList listaSlojeva = document.getElementsByTagName("sloj");
            for (int i = 0; i < listaSlojeva.getLength(); i++) {
                Node node = listaSlojeva.item(i);
                Element element = (Element) node;
                String directory = element.getAttribute("directory");
                Sl sloj = new Sl(directory);
                slika.slojevi.add(sloj);
            }

            NodeList listaSelekcija = document.getElementsByTagName("selekcija");
            for (int i = 0; i < listaSelekcija.getLength(); i++) {
                Node node = listaSelekcija.item(i);
                Element element = (Element) node;
                String naziv = element.getAttribute("naziv");
                NodeList listaPravougaonika = element.getChildNodes();
                ArrayList<Pravoug> pravougs = new ArrayList<Pravoug>();
                for (int j = 0; j < listaPravougaonika.getLength(); j++) {
                    Node node1 = listaPravougaonika.item(j);
                    Element element1 = (Element) node1;
                    int y = Integer.parseInt(element1.getAttribute("y"));
                    int x = Integer.parseInt(element1.getAttribute("x"));
                    int width = Integer.parseInt(element1.getAttribute("width"));
                    int height = Integer.parseInt(element1.getAttribute("height"));
                    Pravoug pravoug = new Pravoug(width, height, x, y);
                    pravougs.add(pravoug);
                }
                Sel sel = new Sel(naziv, pravougs);
                slika.selekcije.add(sel);
            }

            NodeList listaOperacija = document.getElementsByTagName("kompozitna");
            for (int i = 0; i < listaOperacija.getLength(); i++) {
                Node node = listaOperacija.item(i);
                Element element = (Element) node;
                String directory = element.getAttribute("directory");
                Oper oper = new Oper(directory);
                slika.operacije.add(oper);
            }

            for(Sl s: slika.slojevi){
                glavna.dodajSloj("Data/"+s.directory);
            }
            for(Sel s: slika.selekcije){
                Set<Pravougaonik> p = new HashSet<Pravougaonik>();
                for(Pravoug pravoug: s.pr){
                    Pravougaonik pravougaonik = new Pravougaonik(pravoug.w,pravoug.h, pravoug.x,pravoug.y);
                    p.add(pravougaonik);
                }
                Selekcija selekcija = new Selekcija(s.naziv, p);
                glavna.dodajSelekciju(selekcija);
            }
            for(Oper o: slika.operacije){
                KompozitnaFunkcija kompozitnaFunkcija = new KompozitnaFunkcija(path + "Data/" + o.directory, glavna);
                kompozitnaFunkcija.parsiraj();
            }
        //eksportuj("test1.xml");

        } catch (ParserConfigurationException pce) {

        } catch (SAXException se) {

        } catch (IOException ioe) {

        }
    }


    public void eksportuj(String directory) {
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("slika");

            for(Sloj s: glavna.getSlojeve()){
                Element element = document.createElement("sloj");
                element.setAttribute("directory", s.getDirectory());
                rootElement.appendChild(element);
            }

            for (Selekcija s: glavna.getSel()){
                for(Pravougaonik p: s.getPravougaonici()){
                    Element element = document.createElement("selekcija");
                    element.setAttribute("naziv", s.getIme());
                    Element element1 = document.createElement("pravougaonik");
                    element1.setAttribute("x", String.valueOf(p.getX()));
                    element1.setAttribute("y", String.valueOf(p.getY()));
                    element1.setAttribute("width", String.valueOf(p.getWidth()));
                    element1.setAttribute("height", String.valueOf(p.getHeight()));
                    element.appendChild(element1);
                    rootElement.appendChild(element);
                }

            }

            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data/" + directory)));


        } catch (ParserConfigurationException pce) {

        } catch (IOException ioe) {

        } catch (TransformerConfigurationException e) {

        } catch (TransformerException e) {

        }
    }


    public void kreiraj(String directorySloja){

        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("slika");


            Element element = document.createElement("sloj");
            element.setAttribute("directory", path + directorySloja);
            rootElement.appendChild(element);


            for (Selekcija s: glavna.getSel()) {
                if (s.getAktivan()) {

                    for (Pravougaonik p : s.getPravougaonici()) {
                        Element element2 = document.createElement("selekcija");
                        element2.setAttribute("naziv", s.getIme());
                        Element element1 = document.createElement("pravougaonik");
                        element1.setAttribute("x", String.valueOf(p.getX()));
                        element1.setAttribute("y", String.valueOf(p.getY()));
                        element1.setAttribute("width", String.valueOf(p.getWidth()));
                        element1.setAttribute("height", String.valueOf(p.getHeight()));
                        element2.appendChild(element1);
                        rootElement.appendChild(element2);
                    }

                }
            }
            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");


            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data\\program.xml")));


        } catch (ParserConfigurationException pce) {

        } catch (IOException ioe) {

        } catch (TransformerConfigurationException e) {

        } catch (TransformerException e) {

        }
    }

    public void trenutnoEksportovanje(){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("slika");

            for(Sloj s: glavna.getSlojeve()){
                if(s.getVidljiv()) {
                    Element element = document.createElement("sloj");
                    element.setAttribute("directory", path + s.getDirectory());
                    rootElement.appendChild(element);
                }
            }


            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data/exp.xml")));


        } catch (ParserConfigurationException pce) {

        } catch (IOException ioe) {

        } catch (TransformerConfigurationException e) {

        } catch (TransformerException e) {

        }
    }

    public void pamUbmp(String directory){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("slika");

            for(Sloj s: glavna.getSlojeve()){
                    Element element = document.createElement("sloj");
                    element.setAttribute("directory", path + s.getDirectory());
                    rootElement.appendChild(element);
            }


            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data/exp.xml")));


        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}
