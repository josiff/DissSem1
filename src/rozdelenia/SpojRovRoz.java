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
public class SpojRovRoz {

    private Random rnd;
    private int min, max;

    public SpojRovRoz(Random rnd, int min, int max) {
        this.rnd = new Random(rnd.nextLong());
        this.min = min;
        this.max = max;
    }

    public double getVal() {
        return min + rnd.nextDouble() * (max - min);
    }

}
