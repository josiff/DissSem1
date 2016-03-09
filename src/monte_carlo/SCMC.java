/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monte_carlo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import rozdelenia.SpojRovRoz;
import rozdelenia.EmpirickeRoz;
import rozdelenia.DisRovRoz;
import java.util.Random;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Jožko
 */
public class SCMC extends ASCMC {

    private Random mainRnd, prav4;

    private DisRovRoz akt1, akt3;
    private EmpirickeRoz akt2, akt4, akt5;
    private SpojRovRoz akt6, akt7, akt8, akt9;
    private int zahod;

    private int pocetRep;
    private int aktRep;

    private double count;
    private boolean running;

    private Hashtable<Integer, Integer> hodnoty = new Hashtable<Integer, Integer>();

    private int WRITE_EVERY = 1000;
   

    public SCMC(int pocetRep, int zahod) {
        mainRnd = new Random();
        this.pocetRep = pocetRep;
        this.zahod = zahod;
        running = true;

        iniWin();
    }

    @Override
    protected void iniWin() {

        if (pocetRep < 10000) {
            WRITE_EVERY = 10;
        }

        akt1 = new DisRovRoz(mainRnd, 4, 15);
        double[] p = {0.2, 0.4, 0.4};
        int[][] trv = {
            {10, 29},
            {30, 48},
            {49, 65}
        };
        akt2 = new EmpirickeRoz(mainRnd, p, trv);

        akt3 = new DisRovRoz(mainRnd, 48, 92);

        double[] p4 = {0.2, 0.8};
        int[][] trv4 = {
            {19, 27},
            {28, 44}
        };
        akt4 = new EmpirickeRoz(mainRnd, p4, trv4);

        double[] p5 = {0.2, 0.5, 0.3};
        int[][] trv5 = {
            {5, 19},
            {20, 39},
            {40, 55}
        };
        akt5 = new EmpirickeRoz(mainRnd, p5, trv5);

        akt6 = new SpojRovRoz(mainRnd, 10, 16);
        akt7 = new SpojRovRoz(mainRnd, 20, 29);
        akt8 = new SpojRovRoz(mainRnd, 12, 17);
        akt9 = new SpojRovRoz(mainRnd, 13, 27);

        prav4 = new Random(mainRnd.nextLong());
    }

    @Override
    protected int monteCarlo() {

        aktRep++;

        double vys1 = 0.0;
        double vys2 = 0.0;
        double vys3 = 0.0;

        double pom = 0.0;
        double result = 0.0;

        boolean flag4 = prav4.nextDouble() < 0.32;

        pom = akt1.getVal();
        vys1 = vys2 = vys3 = pom;

        pom = akt2.getVal();
        vys1 += pom;

        pom = akt3.getVal();
        vys2 += pom;
        vys3 += pom;

        if (!flag4) {
            vys2 += akt4.getVal();
            vys3 += akt7.getVal();

        } else {
            pom = akt7.getVal();
            vys3 += pom * 1.15;
        }

        pom = akt5.getVal();
        vys1 += pom;
        vys2 += pom;

        pom = akt6.getVal();
        vys1 += pom;

        pom = akt8.getVal();
        vys3 += pom;

        pom = akt9.getVal();
        vys1 += pom;
        vys2 += pom;
        vys3 += pom;

        pom = flag4 == false ? getMax(vys1, vys2, vys3) : Math.max(vys1, vys3);
        result += pom;

        int resultInt = (int) Math.ceil(result);

        count += result;
        addHodnotu(resultInt);

        return resultInt;

    }

    /**
     * Pridanie hodnoty do hasshtable
     * @param hodnota 
     */
    private void addHodnotu(int hodnota) {
        int pocet = hodnoty.containsKey(hodnota) ? hodnoty.get(hodnota) : 0;
        hodnoty.put(hodnota, pocet + 1);
    }

    /**
     * Vrati maximum z cisel
     * @param a
     * @param b
     * @param c
     * @return 
     */
    private double getMax(double a, double b, double c) {
        double pom = Math.max(a, b);
        return Math.max(pom, c);
    }

    public double getAvg() {
        return count / aktRep;

    }

    /**
     * Histogram
     *
     * @return
     */
    public IntervalXYDataset getDataset() {
        XYSeries xyser = new XYSeries("Nazov", true);

        for (Integer list1 : sortHashRev(hodnoty)) {

            xyser.add(list1, hodnoty.get(list1));

        }

        XYSeriesCollection dataset = new XYSeriesCollection(xyser);
        return dataset;
    }

    /**
     * Graf pre krivku
     *
     * @return
     */
    public XYSeriesCollection getDataSetKrivka() {
        XYSeries xySeries = new XYSeries("");

        double sum = 0;
        double osy = 0;

        for (Integer key : sortHash(hodnoty)) {
            sum += hodnoty.get(key);
            osy = sum / aktRep * 100;
            xySeries.add(key.intValue(), osy);

        }

        return new XYSeriesCollection(xySeries);
    }

    /**
     * Zotriedenie
     * @param table
     * @return 
     */
    public List<Integer> sortHash(Hashtable<Integer, Integer> table) {
        List<Integer> list = new ArrayList<Integer>();
        for (Integer key : table.keySet()) {
            list.add(key);
        }
        Collections.sort(list);

        return list;

    }

    /**
     * Zotriedenie reverse
     * @param table
     * @return 
     */
    public List<Integer> sortHashRev(Hashtable<Integer, Integer> table) {
        List<Integer> list = new ArrayList<Integer>();
        for (Integer key : table.keySet()) {
            list.add(key);
        }
        Collections.sort(list, Collections.reverseOrder());

        return list;

    }

    /**
     * Vypisanie travanie projektu s prav skoncenia
     * @param percento
     * @return 
     */
    public int vypDni(int percento) {
        double pom = percento * pocetRep / 100;
        double sum = 0;

        for (Integer key : sortHash(hodnoty)) {
            sum += hodnoty.get(key);

            if (sum > pom) {
                return key;
            }
        }
        return 0;
    }

    /**
     * Vratenie s akou prav sa skonci projekt
     * @param dni
     * @return 
     */
    public double getPrav(int dni) {

        double sum = 0.0;
        double result = 0.0;

        List<Integer> list = sortHash(hodnoty);

        for (Integer key : list) {
            if (key.intValue() > dni) {
                break;
            }
            sum += hodnoty.get(key);
            

        }
        result = sum / (double) getPocetRep() * 100;
        return result;

    }

    /**
     * Spustenie simulacie
     *
     * @param avgSeries
     * @param pocetOpakovani
     */
    public void simuluj(XYSeries avgSeries) {
        Thread tr = new Thread() {
            public void run() {

                for (int i = 0; i < getPocetRep(); i++) {

                    if (i >= zahod) {
                        if (i % WRITE_EVERY == 0) {

                            avgSeries.add(i, getAvg());
                            
                        }

                    }
                    monteCarlo();

                }               
                
                setChanged();
                notifyObservers();

            }

        };

        tr.start();

    }

    public String vypis() {
        String result = "";

        List<Integer> list = sortHash(hodnoty);
        for (Integer key : list) {
            result += "Čas: " + key + "dní -- Výskyt: " + hodnoty.get(key) + "\n";
        }

        return result;
    }

    public int getPocetRep() {
        return pocetRep;
    }

}
