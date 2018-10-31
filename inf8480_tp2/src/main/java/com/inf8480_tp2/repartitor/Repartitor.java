package com.inf8480_tp2.repartitor;

import com.inf8480_tp2.shared.directory.NameDirectory;
import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.operations.reader.OperationsReader;
import com.inf8480_tp2.shared.server.ComputeServer;
import com.inf8480_tp2.shared.server.ServerInfo;

import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
    private final Collection<Operation> operationBuffer;
    
    /**
     * A collection of the available computation servers to operation execution.
     * This collection can be flushed when a server crashes for example to get
     * all the servers available.
     */
    private final Collection<ComputeServer> computationServers;
    
    /**
     * Tells if the repartitor has finished all its tasks.
     */
    private boolean isDone = false;
    
    /**
     * The credentials to authenticate the repartitor to the name service.
     */
    private static final String LOGIN = "repartitor";
    private static final String PASSWORD = "repartitorpassword";
    
    /**
     * Launches a repartitor that connects to the computation servers and
     * submits tasks to them, given the operations to perform. It then prints
     * the result of the operations.
     * 
     * @param args 
     */
    public void main(String[] args) {
        try {
            //TODO: read options to know if secure mode is set.
            Repartitor repartitor = new Repartitor("TODO: set the good file"); // TODO: set the good file
            int result = repartitor.run();
            System.out.println(result);
        } catch (FileNotFoundException ex) {
            System.err.println("Operation file cannot be found.");
        } catch (RemoteException ex) {
            System.err.println("RemoteException during repartitor run: " + ex);
        } finally {
            System.exit(1);
        }
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
        this.computationServers = new LinkedList();
        OperationsReader or = new OperationsReader(operationFile);
        or.createOperations(operationBuffer);
    }
    
    /**
     * Runs the repartitor and computes the result of its operation file.
     * 
     * @return The result of its operations.
     * @throws java.rmi.RemoteException when the error is not related to
     */
    public int run() throws RemoteException {
        NameDirectory nameDir = connectNameDirectory();
        nameDir.registerDispatcher(LOGIN, PASSWORD);
        setComputationServers(nameDir);
        while(!isDone) {
            setDone(true);
        }
        return 0;
    }
    
    /**
     * Connects to the name directory to authenticate and get the computation
     * servers.
     * 
     * @return The name directory.
     */
    private NameDirectory connectNameDirectory() {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry("TODO: put @IP");
            return (NameDirectory) registry.lookup("NameDirectory");
        } catch (RemoteException ex) {
            System.err.println("Cannot connect to the name directory.");
        } catch (NotBoundException ex) {
            System.err.println("Name directory appears not to be bound.");
        } finally {
            System.exit(1);
        }
        return null;
    }

    /**
     * Sets the computation server collection by getting all the servers
     * available from the given name directory.
     * 
     * @param nameDir The name directory containing the computation server info.
     * @throws java.rmi.RemoteException
     */
    public void setComputationServers(NameDirectory nameDir) throws RemoteException {
        computationServers.clear();
        for(ServerInfo serverInfo: nameDir.getAvailableServers()){
            try {
                Registry reg = LocateRegistry.getRegistry(serverInfo.getIpAdress());
                computationServers.add((ComputeServer)reg.lookup("ComputeServer"));
            } catch (NotBoundException ex) {
                System.err.println("The computation server appears not to be bound.");
                System.exit(1);
            }
        }
    }
    
    /**
     * Sets the state of the repartitor to the given state.
     * @param state If the repartitor is done or not. 
     */
    public void setDone(boolean state) {
        this.isDone = state;
    }
}
