package com.inf8480_tp2.shared.operations;

import java.util.LinkedList;
import java.util.Queue;

/**
 * An operation can be executed by a computational server and returns
 * a result.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public abstract class Operation {

    /**
     * Get the number of mathematical operations this object is containing
     *
     * @return The number of operations to perform
     */
    public abstract int getNumberOfOperations();

    /**
     * The execution of the operation returns the integer result.
     * 
     * @return the result of the operation. 
     */
    public abstract int execute();
    
    /**
     * Default getter for an operation.
     * 
     * @return A Queue containing the operation.
     */
    public Queue<Operation> getOperations() {
        Queue<Operation> queue = new LinkedList();
        queue.add(this);
        return queue;
    }
    
}
