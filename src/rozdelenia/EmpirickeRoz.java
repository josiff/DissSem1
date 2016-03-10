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
    
    private double[] cond;
    private int[][] trvanie;
    private Random[]  randomList;

    public EmpirickeRoz(Random nasada, double[] p, int[][] trv) {

        prav = new Random(nasada.nextLong());
        
        this.cond = new double[p.length + 1];
        randomList = new Random[p.length];
        for (int i = 0; i < p.length; i++) {
            Random rnd = new Random(nasada.nextLong());
            randomList[i] = rnd;
        }
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

        
    public int getVal() {

        double p = getPrav();        
        //System.out.println("prav " + p);
        for (int i = 0; i < cond.length; i++) {
            if (p >= cond[i] && p < cond[i + 1]) {  
                //System.out.println("min-" + trvanie[i][0] + " max- " + trvanie[i][1]);
                Random rnd = randomList[i];
                
                return (rnd.nextInt(trvanie[i][1] - trvanie[i][0]) + trvanie[i][0]) ;
            };
        }

        return 0;

    }

}
