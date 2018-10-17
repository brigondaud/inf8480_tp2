package com.inf8480_tp2.shared.operations;

import java.util.Collection;
import java.util.LinkedList;

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
    
    public Task() {
        this.operations = new LinkedList();
    }
    
    /**
     * Getter for the task operations.
     * 
     * @return Task's operations.
     */
    public Collection<AtomicOperation> getOperations() {
        return this.operations;
    }
    
    /**
     * Adds an operation to the task.
     * 
     * @param operation The operation to add to the task.
     */
    public void addOperation(AtomicOperation operation) {
        this.operations.add(operation);
    }

    /**
     * Goes through all the operations and executes them, modulo 4000.
     * 
     * @return The result of all the opeations, modulo 4000.
     */
    @Override
    public int execute() {
        int result = 0;
        for(AtomicOperation operation: operations) {
            result += operation.execute();
            result %= 4000;
        }
        return result;
    }
    
}
