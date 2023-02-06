package projekat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrameSlike extends JFrame {

    private BufferedImage img = null;
    private String directory;
    private PomeranjeMisa pomeranjeMisa = new PomeranjeMisa();
    private Meni meni;

    public FrameSlike(String s, Meni meni){
        super(s);
        this.meni = meni;
        this.setLocation(900,50);
        this.addMouseListener(pomeranjeMisa);
        this.addMouseMotionListener(pomeranjeMisa);
    }

    public void zatvori(){
        this.dispose();
    }

    @Override
    public void paint(Graphics g) {

        g.drawImage(img,8,31,this);

        int w = pomeranjeMisa.getCurX() - pomeranjeMisa.getBgX(), h = pomeranjeMisa.getCurY() - pomeranjeMisa.getBgY();
        Dimension dims = getSize();
        if (pomeranjeMisa.getBgX() < 0 || pomeranjeMisa.getBgY() < 0) return;
        Graphics2D graphics = (Graphics2D) g.create();
        float[] dash1 = { 2f, 0f, 2f };
        BasicStroke bs1 = new BasicStroke(2,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND,
                1.0f,
                dash1,
                2f);
        graphics.setStroke(bs1);
        if(meni.kreiranjeSelekcije) {
            graphics.drawLine(pomeranjeMisa.bgX, pomeranjeMisa.bgY, pomeranjeMisa.bgX + w, pomeranjeMisa.bgY);
            graphics.drawLine(pomeranjeMisa.bgX, pomeranjeMisa.bgY, pomeranjeMisa.bgX, pomeranjeMisa.bgY + h);
            graphics.drawLine(pomeranjeMisa.bgX, pomeranjeMisa.bgY + h, pomeranjeMisa.bgX + w, pomeranjeMisa.bgY + h);
            graphics.drawLine(pomeranjeMisa.bgX + w, pomeranjeMisa.bgY, pomeranjeMisa.bgX + w, pomeranjeMisa.bgY + h);
        }
        for(Pravougaonik p: meni.pravougaonici){
            graphics.drawLine(p.getX() + 8,p.getY() + 31,p.getX() + 8 + p.getWidth(), p.getY() + 31);
            graphics.drawLine(p.getX() + 8,p.getY() + 31,p.getX() + 8,p.getY() + 31 + p.getHeight());
            graphics.drawLine(p.getX() + 8,p.getY() + p.getHeight() + 31,p.getX() + 8 + p.getWidth(),p.getY() + 31 + p.getHeight());
            graphics.drawLine(p.getX() + 8 + p.getWidth(),p.getY() + 31,p.getX() + 8 + p.getWidth(),p.getY() + 31 + p.getHeight());
        }
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void ucitajSliku() {
        try {
            setVisible(true);
            String path = "C:\\Users\\Aleksa\\IdeaProjects\\projekatPOOPpokusaj2\\";
            Pattern rx1 = Pattern.compile("([^.]*).pam");
            Pattern rx2 = Pattern.compile("([^.]*).bmp");
            Matcher matcher1 = rx1.matcher(directory);
            Matcher matcher2 = rx2.matcher(directory);

            if (matcher1.matches()) {
                XMLformat xmLformat = new XMLformat(meni.getGlavna());
                xmLformat.pamUbmp(directory);
                KompozitnaFunkcija kompozitnaFunkcija = new KompozitnaFunkcija();
                kompozitnaFunkcija.kreirajEksp(path + "Data/tmptmp.bmp");

                String file = Meni.exePath + "Data\\exp.xml " + "Data\\oper.xml";
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process process = runtime.exec(file);
                    process.waitFor();

                    img = ImageIO.read(new File(path + "Data/tmptmp.bmp"));
                    this.setSize(new Dimension(img.getWidth() + 16, img.getHeight() + 39));
                    repaint();
                } catch (InterruptedException ex) {

                } catch (IOException ex) {
                    new Greska("Fajl nije pronadjen!");
                }
            }
            else if (matcher2.matches()){
                img = ImageIO.read(new File(path + directory));
                this.setSize(new Dimension(img.getWidth() + 16, img.getHeight() + 39));
                repaint();
            }
            else throw new Greska("Greska pri citanju slike!");
        } catch (IOException e) {
            new Greska("Greska pri citanju slike!");
        }
        catch (NullPointerException ex){

        }
        catch (Greska g) {}

    }

    private class PomeranjeMisa implements MouseListener, MouseMotionListener{

        private boolean dragging = false;
        private int curX = -1, curY = -1;
        private int bgX = -1, bgY = -1;


        public PomeranjeMisa() {
            super();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            requestFocus();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Point point = e.getPoint();
            bgX = point.x;
            bgY = point.y;
            dragging = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dragging = false;
            if(meni.kreiranjeSelekcije == true){
                int w = pomeranjeMisa.getCurX() - pomeranjeMisa.getBgX(), h = pomeranjeMisa.getCurY() - pomeranjeMisa.getBgY();
                if(w > img.getWidth()) w = img.getWidth();
                if(h > img.getHeight()) h = img.getHeight();
                int x = pomeranjeMisa.getBgX()-8;
                if(x < 0) x = 0;
                int y = pomeranjeMisa.getBgY()-31;
                if(y < 0) y = 0;
                Pravougaonik p = new Pravougaonik(w, h, x, y);
                meni.pravougaonici.add(p);
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point p = e.getPoint();
            curX = p.x;
            curY = p.y;
            if (dragging) repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        public boolean isDragging() {
            return dragging;
        }

        public int getCurX() {
            return curX;
        }

        public int getCurY() {
            return curY;
        }

        public int getBgX() {
            return bgX;
        }

        public int getBgY() {
            return bgY;
        }
    }
}