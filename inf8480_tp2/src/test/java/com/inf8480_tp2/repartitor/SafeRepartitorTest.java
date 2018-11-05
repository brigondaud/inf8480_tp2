package com.inf8480_tp2.repartitor;

import com.inf8480_tp2.directory.NameDirectoryImpl;
import com.inf8480_tp2.server.ComputeServerImpl;
import com.inf8480_tp2.server.ComputeServerRunner;
import com.inf8480_tp2.shared.parser.OptionParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
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
    
    private static final int dirPort = 5005;
    
    /**
     * The name directory used in during the tests.
     */
    private static final NameDirectoryImpl nameDir = new NameDirectoryImpl();
    
    /**
     * The repartitors used in every tests. Each repartitor has a test file and
     * thus an expected result.
     */
    private Map<File, Repartitor> repartitors;
    
    /**
     * A runner to start computation servers.
     */
    private ComputeServerRunner runner;
    
    private static final String operationsFolder = System.getProperty("user.dir") 
            + File.separator
            + "operations";

    public SafeRepartitorTest() {
        this.repartitors = new HashMap();
        this.runner = new ComputeServerRunner();
    }
    
    /**
     * Creates a RMI registry locally to run the tests and starts the name
     * directory.
     */
    @BeforeClass
    public static void setUpClass() {
        try {
            LocateRegistry.createRegistry(dirPort);
        } catch (RemoteException ex) {
            System.err.println("Cannot start the safe integration test: the"
                    + " registry cannot be created.");
            System.exit(1);
        }
        nameDir.run(new OptionParser(new String[]{"--portDir", ""+dirPort}));
    }
    
    /**
     * Goes through every test case in the operations folder and creates a
     * repartitor for it.
     */
    @Before
    public void setUp() throws FileNotFoundException, UnknownHostException {
        File operationDirectory = new File(operationsFolder);
        for(File file: operationDirectory.listFiles()) {
            repartitors.put(file, new Repartitor(new OptionParser(
            new String[] {
                "--ipDir",
                InetAddress.getLocalHost().getHostAddress(),
                "--portDir",
                ""+dirPort,
                "--operations",
                "operations" + File.separator + file.getName()
            })));
        }
    }
    
    /**
     * Make sure to empty the name directory and the repartitors
     * after each tests.
     */
    @After
    public void tearDown() {
        nameDir.flush();
        repartitors.clear();
    }
    
    /**
     * Run all the operations files.
     * 
     * @throws RemoteException 
     */
    private void runAllOperations() throws RemoteException {
        for(File file: repartitors.keySet()) {
            Repartitor repartitor = repartitors.get(file);
            repartitor.run();
            assertEquals(Integer.parseInt(file.getName().split("-")[1]),
                    repartitor.getResult());
        }
    }

    /**
     * Computes the operations with only one computation server.
     */
    @Test
    public void oneServerTest() throws UnknownHostException, RemoteException {
        System.out.println("\nRunning one normal server:");
        runner.runSafeServers(1, dirPort, 5007);
        runAllOperations();
    }
    
    /**
     * Computes the operations with two computation servers.
     */
    @Test
    public void twoServerTest() throws UnknownHostException, RemoteException {
        System.out.println("\nRunning two normal servers:");
        runner.runSafeServers(2, dirPort, 5010);
        runAllOperations();
    }
    
    /**
     * Computes the operations with three computation servers.
     */
    @Test
    public void threeServerTest() throws UnknownHostException, RemoteException {
        System.out.println("\nRunning three normal servers:");
        runner.runSafeServers(3, dirPort, 5015);
        runAllOperations();
    }
}
