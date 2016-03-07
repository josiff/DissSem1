/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monte_carlo;

import java.util.HashMap;
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
    private int stop140;
    
    private double count;
    private boolean running;
    
   private Hashtable<Integer, Integer> hodnoty = new Hashtable<Integer, Integer>();
  
    
    private int END_DAY = 140; 
    private int WRITE_EVERY = 1000;
    private double PERCENTA_SKONCENIA = 0.8;
    
    
    

    public SCMC(int pocetRep, int zahod) {
        mainRnd = new Random();
        this.pocetRep = pocetRep;
        this.zahod = zahod;
        running = true;
        iniWin();
    }

    @Override
    protected void iniWin() {
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
        
        
        int resultInt = (int)Math.ceil(result);
        
        if(resultInt <= END_DAY){
            stop140++;
        }
               
        count += result;
        addHodnotu(resultInt);

        return resultInt;

    }
    
    private void addHodnotu(int hodnota){
        int pocet = hodnoty.containsKey(hodnota) ? hodnoty.get(hodnota) : 0;
        hodnoty.put(hodnota, pocet + 1);
    }

    private double getMax(double a, double b, double c) {
        double pom = Math.max(a, b);
        return Math.max(pom, c);
    }
    
    
    private double getAvg(){
        return count/aktRep;
        
    }

   public IntervalXYDataset getDataset() {
        XYSeries xyser = new XYSeries("Nazov", true);

//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer key : hodnoty.keySet()) {
           
            xyser.add(key, hodnoty.get(key));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(xyser);
        return dataset;
    }
   
   
   public XYSeriesCollection getDataSetKrivka(){
        XYSeries xySeries = new XYSeries("");
        int hladanaHodnota = 0;
        double hodnotaTemp = 0;        
        double hodnotaDo = PERCENTA_SKONCENIA * pocetRep;
        for (Integer key : hodnoty.keySet()) {
            
            hodnotaTemp += hodnoty.get(key);
            xySeries.add((double) key, hodnotaTemp);            

            if (hodnotaTemp > hodnotaDo) {
                hladanaHodnota = key;
                break;
            }

        }
        
        
        return new XYSeriesCollection(xySeries);
   }

    public void simuluj(XYSeries avgSeries, int pocetOpakovani) {
        Thread tr = new Thread() {
            public void run() {

                for (int i = 0; i < pocetOpakovani; i++) {
                    pause();

                    if (i >= zahod) {
                        if (i % WRITE_EVERY == 0) {
                           
                            avgSeries.add(i, getAvg());
                           // System.out.println(getAvg());
                        }

                    }
                    double hod =  monteCarlo();
                    //System.out.println(hod);
                    }

                //}
                  System.out.println(getAvg());
            }

        };

        tr.start();

    }
    
    
    private void pause(){
         while (!this.running) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
    }

}
