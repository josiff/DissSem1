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
public class DisRovRoz {

    private Random rnd;
    private int min, max;

    public DisRovRoz(Random rnd, int min, int max) {
        this.rnd = new Random(rnd.nextLong());
        this.min = min;
        this.max = max;
    }

    public int getVal() {
        return rnd.nextInt(max - min + 1) + min;
    }

}
