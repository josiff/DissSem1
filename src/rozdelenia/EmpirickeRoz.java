/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rozdelenia;

import java.util.Random;

/**
 *
 * @author Jožko
 */
public class EmpirickeRoz {

    private Random prav;
    private Random rnd;
    private double[] cond;
    private int[][] trvanie;

    public EmpirickeRoz(Random nasada, double[] p, int[][] trv) {

        prav = new Random(nasada.nextLong());
        rnd = new Random(nasada.nextLong());
        this.cond = new double[p.length + 1];
        this.trvanie = trv;
        initCond(p);

    }

    /**
     * Vytvorenie intervalov podla % pravdepodobnosti
     * @param p 
     */
    private void initCond(double []p) {
        
        cond[0] = 0;
        for (int i = 0; i < p.length; i++) {
            cond[i+1] = p[i] + cond[i];
        }
        
    }

    /**
     * Generovanie pravdepodobnosti
     * @return 
     */
    public double getPrav() {
        return prav.nextDouble();
    }

    /**
     * Generovanie hodnoty z intervalu
     * @param min
     * @param max
     * @return 
     */
    public int getRnd(int min, int max) {
        return rnd.nextInt(max - min + 1) + min;
    }

    
    public int getVal() {

        double p = getPrav();
        
        for (int i = 0; i < cond.length; i++) {
            if (p >= cond[i] && p < cond[i + 1]) {                
                return getRnd(trvanie[i][0], trvanie[i][1]);
            };
        }

        return 0;

    }

}
