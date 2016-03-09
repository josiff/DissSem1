/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monte_carlo;

import java.util.Observable;

/**
 *
 * @author Jožko
 */
public abstract class ASCMC extends Observable{
    
    protected abstract void iniWin();
    
    protected abstract int monteCarlo();
    
}
