package com.inf8480_tp2.shared.operations;

/**
 * The prime operation computes the biggest prime factor.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class Prime extends AtomicOperation {

    public Prime(int parameter) {
        super(parameter);
    }

    @Override
    public int execute() {
        return Operations.prime(parameter);
    }
    
}
