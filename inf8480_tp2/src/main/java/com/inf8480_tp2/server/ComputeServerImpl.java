package com.inf8480_tp2.server;

import com.inf8480_tp2.shared.directory.NameDirectory;
import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.parser.OptionParser;
import com.inf8480_tp2.shared.response.Response;
import com.inf8480_tp2.shared.server.ComputeServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 * Implements a server class that will execute some operations assigned by a dispatcher using RMI.
 * Each server has a readable capacity from RMI call.
 *
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class ComputeServerImpl implements ComputeServer {

    private int serverCapacity;
    private NameDirectory nameDirectory;

    public ComputeServerImpl(int capacity) {
        this.serverCapacity = capacity;
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            OptionParser parser = new OptionParser(args);
            int serverCapacity = parser.getServerCapacity();
            String directoryAddress = parser.getDirectoryAddress();
            int serverPort = parser.getServerPort();
            int directoryPort = parser.getDirectoryPort();
            ComputeServer server = new ComputeServerImpl(serverCapacity);
            // Get the RMI Registry associated to the directory and lookup the directory stub
            Registry directoryRegistry = LocateRegistry.getRegistry(directoryAddress, directoryPort);
            NameDirectory nameDirectory = (NameDirectory) directoryRegistry.lookup("NameDirectory");
            ((ComputeServerImpl) server).nameDirectory = nameDirectory;
            nameDirectory.bind(serverCapacity, serverPort);
            // Then a stub is bound to a registry locally
            ComputeServer stub = (ComputeServer) UnicastRemoteObject.exportObject(server, serverPort + 1);
            Registry serverRegistry = LocateRegistry.getRegistry(serverPort);
            serverRegistry.rebind("ComputeServer", stub);
            System.out.println("Compute server ready.");
        } catch (RemoteException e) {
            System.err.println("Remote exception happened during Server creation: ");
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.err.println("NotBoundException happened during Server creation: ");
            e.printStackTrace();
        } catch (ServerNotActiveException e) {
            System.err.println("An exception happened during object binding to name directory: ");
            e.printStackTrace();
        }
    }

    @Override
    public Response executeOperation(Operation operation, String login, String password) throws RemoteException {
        if (!this.nameDirectory.authenticateDispatcher(login, password)) {
            // TODO Return ErrorResponse
            return null;
        }
        Random random = new Random();
        float randomNum = random.nextFloat() * 100;
        float refusalRate = this.refusalRate(operation);
        if (refusalRate > 0 && randomNum <= refusalRate) {
            // TODO
            // Tell the dispatcher the server refused the task
        } else {
            operation.execute();
        }
        return null;
    }

    /**
     * Returns server's refusal rate given a task to perform using simulation formula
     *
     * @return The refusal rate of the server for a specific task
     */
    private float refusalRate(Operation operation) {
        int taskSize = operation.getNumberOfOperations();
        if (taskSize < this.serverCapacity) {
            return 0;
        } else if (taskSize > 5 * this.serverCapacity) {
            return 100;
        } else {
            return ((taskSize - this.serverCapacity) / (4 * this.serverCapacity)) * 100;
        }
    }

}
