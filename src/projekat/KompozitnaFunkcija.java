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

public class KompozitnaFunkcija {

    private class _Operacija{
        private String naziv;
        private int val;
        private String rgb;

    }

    private ArrayList<_Operacija> operacije = new ArrayList<_Operacija>();
    private String directory = null;
    private Slika glavna = null;

    public KompozitnaFunkcija(){

    }

    public KompozitnaFunkcija(String directory, Slika glavna) {
        this.directory = directory;
        this.glavna = glavna;
    }

    public void parsiraj(){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(directory);

            NodeList listaOperacija = document.getElementsByTagName("operacija");

            for (int i = 0; i < listaOperacija.getLength(); i++) {
                Node node = listaOperacija.item(i);
                Element element = (Element) node;
                _Operacija operacija = new _Operacija();
                String naziv = element.getAttribute("naziv");
                operacija.naziv = naziv;
                if (!naziv.equals("nijansasive") && !naziv.equals("crnobelo") && !naziv.equals("medijana")) {
                    String rgb = element.getAttribute("piksel");
                    operacija.rgb = rgb;
                }
                if (!naziv.equals("inverzija") && !naziv.equals("abs") && !naziv.equals("nijansasive") && !naziv.equals("crnobelo") && !naziv.equals("medijana")) {
                    int val = Integer.parseInt(element.getAttribute("val"));
                    operacija.val = val;
                }

                operacije.add(operacija);
            }


            for(var o: operacije){
                KompozitnaFunkcija kompozitna = new KompozitnaFunkcija();
                String naziv = o.naziv;
                if (naziv.equals( "nijansasive") || naziv.equals( "crnobelo") || naziv.equals("medijana")) {
                    if (naziv.equals("nijansasive")) kompozitna.kreiraj(glavna, "nijansasive");
                    if (naziv.equals( "crnobelo")) kompozitna.kreiraj(glavna, "crnobelo");
                    if (naziv.equals( "medijana")) kompozitna.kreiraj(glavna, "medijana");
                } else {
                    if (naziv.equals( "sabiranje"))
                        kompozitna.kreiraj(glavna, "sabiranje", o.rgb, o.val);
                    if (naziv.equals( "oduzimanje"))
                        kompozitna.kreiraj(glavna, "oduzimanje", o.rgb, o.val);
                    if (naziv.equals( "inverznooduzimanje"))
                        kompozitna.kreiraj(glavna, "inverznooduzimanje", o.rgb, o.val);
                    if (naziv.equals("mnozenje"))
                        kompozitna.kreiraj(glavna, "mnozenje", o.rgb, o.val);
                    if (naziv.equals( "deljenje"))
                        kompozitna.kreiraj(glavna, "deljenje", o.rgb, o.val);
                    if (naziv.equals( "inverznodeljenje"))
                        kompozitna.kreiraj(glavna, "inverznodeljenje", o.rgb, o.val);
                    if (naziv.equals("power"))
                        kompozitna.kreiraj(glavna, "power", o.rgb, o.val);
                    if (naziv.equals( "log"))
                        kompozitna.kreiraj(glavna, "log", o.rgb, o.val);
                    if (naziv.equals( "min"))
                        kompozitna.kreiraj(glavna, "min", o.rgb, o.val);
                    if (naziv.equals("max"))
                        kompozitna.kreiraj(glavna, "max", o.rgb, o.val);
                    if (naziv.equals("abs")) kompozitna.kreiraj(glavna, "abs", o.rgb);
                    if (naziv.equals("inverzija")) kompozitna.kreiraj(glavna, "inverzija", o.rgb);
                }


                for (Sloj s : glavna.getSlojeve()) {
                    if (s.getAktivan() == true) {
                        XMLformat xmLformat = new XMLformat(glavna);
                        xmLformat.kreiraj(s.getDirectory());

                        String file = Meni.exePath + "Data\\program.xml " + "Data\\operacija.xml";
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
            }

        } catch (ParserConfigurationException pce) {

        } catch (SAXException se) {

        } catch (IOException ioe) {

        }
    }


    public void pisi(String izlaz, ArrayList<Operacija> operacije){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("operacije");

            for(var o: operacije){
                Element element = document.createElement("operacija");
                element.setAttribute("naziv", o.getNaziv());
                if(!(o.getRgb() == null)){
                    element.setAttribute("piksel", o.getRgb());
                }
                if(!(o.getVal() == -1)){
                    element.setAttribute("val", String.valueOf(o.getVal()));
                }
                rootElement.appendChild(element);
            }


            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");


            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data/" + izlaz)));

        } catch (ParserConfigurationException pce) {

        } catch (IOException ioe) {

        } catch (TransformerException e) {

        }

    }

    public void kreiraj(Slika glavna, String nazivOperacije){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("operacije");

            Element element = document.createElement("operacija");
            element.setAttribute("naziv", nazivOperacije);
            rootElement.appendChild(element);

            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");


            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data/operacija.xml")));

        } catch (ParserConfigurationException pce) {

        } catch (IOException ioe) {

        } catch (TransformerException e) {

        }
    }

    public void kreiraj(Slika glavna, String nazivOperacije, String rgb){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("operacije");

            Element element = document.createElement("operacija");
            element.setAttribute("naziv", nazivOperacije);
            element.setAttribute("piksel", rgb);
            rootElement.appendChild(element);

            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");


            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data\\operacija.xml")));

        } catch (ParserConfigurationException pce) {

        } catch (IOException ioe) {

        } catch (TransformerException e) {

        }
    }

    public void kreiraj(Slika glavna, String nazivOperacije, String rgb, int val){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("operacije");

            Element element = document.createElement("operacija");
            element.setAttribute("naziv", nazivOperacije);
            element.setAttribute("piksel", rgb);
            element.setAttribute("val", String.valueOf(val));
            rootElement.appendChild(element);

            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");


            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data/operacija.xml")));

        } catch (ParserConfigurationException pce) {

        } catch (IOException ioe) {

        } catch (TransformerException e) {

        }
    }

    public void kreirajPopuniBojom(Slika glavna, String nazivOperacije, int r, int g, int b){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("operacije");

            Element element = document.createElement("operacija");
            element.setAttribute("naziv", nazivOperacije);
            element.setAttribute("r", String.valueOf(r));
            element.setAttribute("g", String.valueOf(g));
            element.setAttribute("b", String.valueOf(b));
            rootElement.appendChild(element);

            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");


            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data/operacija.xml")));

        } catch (ParserConfigurationException pce) {

        } catch (IOException ioe) {

        } catch (TransformerException e) {

        }
    }

    public void kreirajProzirnost(Slika glavna, int brojSloja, int val){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("operacije");

            Element element = document.createElement("operacija");
            element.setAttribute("naziv", "prozirnost");
            element.setAttribute("sloj", "0");
            element.setAttribute("val", String.valueOf(val));
            rootElement.appendChild(element);

            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");


            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data/operacija.xml")));

        } catch (ParserConfigurationException pce) {

        } catch (IOException ioe) {

        } catch (TransformerException e) {

        }
    }

    public void kreirajEksp(String directory){
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("operacije");

            Element element = document.createElement("operacija");
            element.setAttribute("naziv", "eksport");
            element.setAttribute("directory", directory);
            rootElement.appendChild(element);

            document.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");


            tr.transform(new DOMSource(document),
                    new StreamResult(new FileOutputStream("Data/oper.xml")));

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
