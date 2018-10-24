package com.inf8480_tp2.repartitor;

import com.inf8480_tp2.shared.operations.AtomicOperation;
import com.inf8480_tp2.shared.operations.reader.OperationsReader;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The Repartitor splits the computations on all the calculator servers. It 
 * gathers the results and checks them.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class Repartitor {
    
    /**
     * The operation buffer contains every operation a repartitor has to
     * distribute.
     */
    Collection<AtomicOperation> operationBuffer;
    
    /**
     * Launches a repartitor that connects to the computation servers and
     * submits tasks to them, given the operations to perform. It then prints
     * the result of the operations.
     * 
     * @param args 
     */
    public void main(String[] args) {
        //TODO
    }
    
    /**
     * The repartitor is built for a given operation file.
     * 
     * @param operationFile The operation file to read from.
     * @throws java.io.FileNotFoundException if the operation file does not
     * exist.
     */
    public Repartitor(String operationFile) throws FileNotFoundException {
        this.operationBuffer = new LinkedList();
        OperationsReader or = new OperationsReader(operationFile);
        or.createOperations(operationBuffer);
    }
    
}
