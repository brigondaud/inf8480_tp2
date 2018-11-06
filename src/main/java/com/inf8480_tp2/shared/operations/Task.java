package com.inf8480_tp2.shared.operations;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A Task is made of several atomic operations.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class Task extends Operation {
    
    /**
     * All the operations that must be computed by the task.
     */
    Queue<Operation> operations;
    
    public Task() {
        this.operations = new LinkedList();
    }
    
    /**
     * Getter for the task operations.
     * 
     * @return Task's operations.
     */
    @Override
    public Queue<Operation> getOperations() {
        return this.operations;
    }
    
    /**
     * Adds an operation to the task.
     * 
     * @param operation The operation to add to the task.
     */
    public void addOperation(Operation operation) {
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
        for(Operation operation: operations) {
            result += operation.execute();
            result %= 4000;
        }
        return result;
    }

    @Override
    public int getNumberOfOperations() {
        return this.operations.size();
    }
    
}
