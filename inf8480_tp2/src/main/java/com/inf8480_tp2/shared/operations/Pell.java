package com.inf8480_tp2.shared.operations;

/**
 * The pell operation computes the n-th pell number.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class Pell extends AtomicOperation {

    public Pell(int parameter) {
        super(parameter);
    }
    
    @Override
    public int execute() {
        return Operations.pell(parameter);
    }
    
}
