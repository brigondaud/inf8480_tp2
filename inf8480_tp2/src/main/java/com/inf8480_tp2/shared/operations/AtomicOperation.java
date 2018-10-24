package com.inf8480_tp2.shared.operations;

/**
 * An atomic operation make only one computation thanks given a paramter.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public abstract class AtomicOperation extends Operation {
    
    /**
     * The parameter of the computation to execute.
     */
    protected int parameter;

    public AtomicOperation(int parameter) {
        this.parameter = parameter;
    }

    /**
     * Getter for the operation parameter.
     * 
     * @return Operation's parameter.
     */
    public int getParameter() {
        return parameter;
    }
}
