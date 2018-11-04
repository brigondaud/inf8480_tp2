package com.inf8480_tp2.repartitor;

import com.inf8480_tp2.directory.NameDirectoryImpl;
import com.inf8480_tp2.shared.parser.OptionParser;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test is an integration test for the whole system. It uses a repartitor
 * and comptuation servers in safe mode. It runs several operations file on
 * an increasing number of servers.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class SafeRepartitorTest {
    
    private static final int rmiPort = 5001;
    
    /**
     * The name directory used in during the tests.
     */
    private static final NameDirectoryImpl nameDir = new NameDirectoryImpl();
    
    /**
     * The repartitors used in every tests. Each repartitor has a test file and
     * thus an expected result.
     */
    private Map<File, Repartitor> repartitors;
    
    private static final String operationsFolder = "";
    
    /**
     * Creates a RMI registry locally to run the tests and starts the name
     * directory.
     */
    @BeforeClass
    public static void setUpClass() {
        try {
            LocateRegistry.createRegistry(rmiPort);
        } catch (RemoteException ex) {
            System.err.println("Cannot start the integration test: the"
                    + " registry cannot be created.");
        }
        nameDir.run(new OptionParser(new String[]{"--portDir", "5002"}));
    }
    
    /**
     * Goes through every test case in the operations folder and creates a
     * repartitor for it.
     */
    @Before
    public void setUp() {
        System.out.println(System.getProperty("user.dir") + File.separator);
    }
    
    /**
     * Make sure to empty the name directory after each tests.
     */
    @After
    public void tearDown() {
        nameDir.flush();
    }

    /**
     * Computes the operations with only one computation server.
     */
//    @Test
//    public void oneServerTest() {
//        assertTrue(true);
//    }
}
