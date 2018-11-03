package com.inf8480_tp2.repartitor;

import com.inf8480_tp2.repartitor.executor.CheckExecutor;
import com.inf8480_tp2.repartitor.executor.Executor;
import com.inf8480_tp2.repartitor.executor.NoCheckExecutor;
import com.inf8480_tp2.repartitor.factory.TaskFactory;
import com.inf8480_tp2.repartitor.strategy.OptimalRepartitionStrategy;
import com.inf8480_tp2.shared.directory.NameDirectory;
import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.operations.Task;
import com.inf8480_tp2.shared.operations.reader.OperationsReader;
import com.inf8480_tp2.shared.parser.OptionParser;
import com.inf8480_tp2.shared.server.ComputeServer;
import com.inf8480_tp2.shared.server.ServerInfo;

import java.awt.*;
import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
    private final Queue<Operation> operationBuffer;
    
    /**
     * A map of the available computation servers to operation execution.
     * This collection can be flushed when a server crashes for example to get
     * all the servers available.
     */
    private final Map<ServerInfo, ComputeServer> computationServers;
    
    /**
     * Tells which server are already in use based on their server info.
     * The server is in used if its value is true.
     */
    private final Map<ServerInfo, Boolean> serverState;
    
    /**
     * The executor which will send the task to the servers.
     */
    private final Executor executor;
    
    /**
     * Tells if the repartitor has finished all its tasks.
     */
    private boolean isDone = false;
    
    /**
     * The result of the repartitor operations.
     */
    private int result;
    
    /**
     * The options with which the repartitor has been launched.
     */
    private OptionParser options;
    
    /**
     * The credentials to authenticate the repartitor to the name service.
     */
    public static final String LOGIN = "repartitor";
    public static final String PASSWORD = "repartitorpassword";
    
    /**
     * Launches a repartitor that connects to the computation servers and
     * submits tasks to them, given the operations to perform. It then prints
     * the result of the operations.
     * 
     * @param args 
     */
    public static void main(String[] args) {
        try {
            Repartitor repartitor = new Repartitor(new OptionParser(args));
            repartitor.run();
            System.out.println(repartitor.getResult());
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
     * @param options The options from repartitor launch.
     * @throws java.io.FileNotFoundException if the operation file does not
     * exist.
     */
    public Repartitor(OptionParser options) throws FileNotFoundException {
        this.operationBuffer = new LinkedList();
        this.computationServers = new HashMap();
        this.serverState = new HashMap();
        this.result = 0;
        this.options = options;
        if(options.getOperationFilePath() == null) {
            System.err.println("No operations file provided.");
            System.exit(1);
        }
        if(options.isSafeMode())
            this.executor = new NoCheckExecutor(this);
        else
            this.executor = new CheckExecutor(this);
        OperationsReader or = new OperationsReader(options.getOperationFilePath());
        or.createOperations(operationBuffer);
    }
    
    /**
     * Runs the repartitor and computes the result of its operation file.
     * 
     * @throws java.rmi.RemoteException when the error is not related to
     */
    public void run() throws RemoteException {
        NameDirectory nameDir = connectNameDirectory();
        nameDir.registerDispatcher(LOGIN, PASSWORD);
        TaskFactory taskFactory = new TaskFactory();
        setComputationServers(nameDir);
        executor.setThreadNumber(computationServers.size());
        while(!isDone) {
            // Using an iterator to be able to remove safely servers during
            // runtime (in case of server crash).
            if(computationServers.isEmpty()) {
                System.err.println("ERROR: no more computation server is available."
                        + " The operations cannot be done.");
                System.exit(1);
            }
            Iterator<Map.Entry<ServerInfo, ComputeServer>> iter = computationServers.entrySet().iterator();
            while(iter.hasNext() && !operationBuffer.isEmpty()) {
                Map.Entry<ServerInfo, ComputeServer> server = iter.next();
                if(serverState.get(server.getKey()))
                    continue;
                setServerState(server.getKey(), true);
                //TODO: check when unsafe if a task is waiting to be scheduled on a second server
                taskFactory.setStrategy(new OptimalRepartitionStrategy(server.getKey().getCapacity()));
                Task task = taskFactory.buildTask(operationBuffer);
                executor.submit(task, server.getKey());
            }
            if(serversDone()) {
                if(operationBuffer.isEmpty()) {
                    executor.shutdown();
                    isDone = true;
                }
            }
        }
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
            registry = LocateRegistry.getRegistry(options.getDirectoryAddress(), options.getDirectoryPort());
            return (NameDirectory) registry.lookup("NameDirectory");
        } catch (RemoteException ex) {
            System.err.println("Cannot connect to the name directory.");
            System.exit(1);
        } catch (NotBoundException ex) {
            System.err.println("Name directory appears not to be bound.");
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
        for(ServerInfo serverInfo: nameDir.getAvailableServers()){
            try {
                Registry reg = LocateRegistry.getRegistry(serverInfo.getIpAdress(), serverInfo.getPort());
                ComputeServer server = (ComputeServer)reg.lookup("ComputeServer");
                computationServers.put(serverInfo, server);
                setServerState(serverInfo, false);
            } catch (NotBoundException ex) {
                System.err.println("The computation server appears not to be bound.");
                System.exit(1);
            }
        }
    }
    
    /**
     * Removes a computation server from the list of the available server. It
     * also removes its state from the current server usage.
     * 
     * @param server The server to remove from the list.
     */
    public void removeComputationServer(ServerInfo server) {
        computationServers.remove(server);
        serverState.remove(server);
    }
    
    /**
     * Sets the state of the repartitor to the given state.
     * @param state If the repartitor is done or not. 
     */
    public void setDone(boolean state) {
        this.isDone = state;
    }
    
    /**
     * Getter for the repartitor's result.
     * 
     * @return Repartitor's result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Adds to the current result the result of an operation. The result must
     * be verified beforehand.
     * 
     * @param operationResult The result of an operation.
     */
    public synchronized void computeResult(int operationResult) {
        this.result += operationResult;
        this.result %= 4000;
    }
    
    /**
     * Adds operation to the repartitor's operation buffer. Used when a task
     * must be resent to a server, thus needs to be 'uncompiled' back to
     * the repartitor.
     * 
     * @param queue The operations to add to the repartitor.
     */
    public synchronized void addOperations(Queue<Operation> queue) {
        operationBuffer.addAll(queue);
    }
    
    /**
     * Getter for the list of the repartitor's computation servers.
     * 
     * @return The repartitor's computation servers.
     */
    public Map<ServerInfo, ComputeServer> getComputationServers() {
        return computationServers;
    }
    
    /**
     * Updates the occupation state of a computation server.
     * 
     * @param server The server for which the state needs to be updated.
     * @param state The new state of the server.
     */
    public synchronized void setServerState(ServerInfo server, Boolean state) {
        if(serverState.containsKey(server))
            serverState.replace(server, state);
        else
            serverState.put(server, state);
    }
    
    /**
     * Removes a server and its state from the repartitor.
     * 
     * @param server The server to remove.
     */
    public synchronized void removeServer(ServerInfo server) {
        computationServers.remove(server);
        serverState.remove(server);
    }
    
    /**
     * Tells if every server is done (which means that every server is
     * available).
     * 
     * @return true if every server state is false.
     */
    public boolean serversDone() {
        Iterator<Map.Entry<ServerInfo, ComputeServer>> iter = computationServers.entrySet().iterator();
        while(iter.hasNext()) {
            if(serverState.get(iter.next().getKey()))
                return false;
        }
        return true;
    }
}
