package com.inf8480_tp2.shared.operations;

import java.util.Collection;

/**
 * A Task is made of several atomic operations.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class Task extends Operation {
    
    /**
     * All the operations that must be computed by the task.
     */
    Collection<AtomicOperation> operations;

    @Override
    public int execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
