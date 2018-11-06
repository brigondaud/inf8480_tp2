/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inf8480_tp2.shared.operations.reader;

import com.inf8480_tp2.shared.operations.AtomicOperation;
import com.inf8480_tp2.shared.operations.Pell;
import com.inf8480_tp2.shared.operations.Prime;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the operations reader by reading a simple operations file and
 * checking the operations produced.
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class OperationsReaderTest {

    @Test
    public void readerTest() throws FileNotFoundException {
        String testFile = "src/test/java/com/inf8480_tp2/shared/operations/reader/operations-test";
        OperationsReader or = new OperationsReader(testFile);
        LinkedList<AtomicOperation>buffer = new LinkedList();
        or.createOperations(buffer);
        AtomicOperation readOp = buffer.poll();
        assertTrue(readOp instanceof Prime);
        assertTrue(readOp.getParameter() == 5571);
        readOp = buffer.poll();
        assertTrue(readOp instanceof Pell);
        assertTrue(readOp.getParameter() == 505);
        readOp = buffer.poll();
        assertTrue(readOp instanceof Pell);
        assertTrue(readOp.getParameter() == 302);
    }
}
