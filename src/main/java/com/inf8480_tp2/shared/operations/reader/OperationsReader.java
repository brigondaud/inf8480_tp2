package com.inf8480_tp2.shared.operations.reader;

import com.inf8480_tp2.shared.operations.Pell;
import com.inf8480_tp2.shared.operations.Prime;

import java.io.*;
import java.util.Collection;

/**
 * The operations reader takes a file as an input and reads through it
 * to populate a given buffer with the correct operations coded in the file.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class OperationsReader {
    
    /**
     * Used to read through an operation file.
     */
    BufferedReader reader;
    
    /**
     * Initiates a reader using the relative path to the operations file.
     * 
     * @param operationsFilePath The file containing the operations.
     */
    public OperationsReader(String operationsFilePath) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(operationsFilePath));
    }

    /**
     * Filss the given buffer with all the operations in the operations file.
     * 
     * @param buffer The buffer to fill with the operations.
     */
    public void createOperations(Collection buffer) {
        try {
            String line = reader.readLine();
            while(line != null) {
                String[] operation = line.split(" ");
                int parameter = Integer.decode(operation[1]);
                switch (operation[0]) {
                    case "pell":
                        buffer.add(new Pell(parameter));
                        break;
                    case "prime":
                        buffer.add(new Prime(parameter));
                        break;
                    default:
                        // Do nothing.
                        // TODO: throw exception instead ?
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ex) {
            System.err.println("Unable to read the operations file.");
        }
        
    }
}
