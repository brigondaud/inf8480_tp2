package com.inf8480_tp2.server;

import com.inf8480_tp2.shared.directory.NameDirectory;
import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.parser.OptionParser;
import com.inf8480_tp2.shared.response.ComputeResponse;
import com.inf8480_tp2.shared.response.InvalidCredentialsResponse;
import com.inf8480_tp2.shared.response.OutOfCapacityResponse;
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
 * Implements a compute server class that will execute some operations assigned by a dispatcher using RMI.
 * Each server has a capacity assigned during construction.
 *
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class ComputeServerImpl implements ComputeServer {

    private int serverCapacity;
    private boolean isSafe;
    private float corruptRate;
    private NameDirectory nameDirectory;

    public ComputeServerImpl(int capacity) {
        this.serverCapacity = capacity;
        this.isSafe = true;
    }

    public ComputeServerImpl(int capacity, float corruptRate) {
        this.serverCapacity = capacity;
        this.isSafe = false;
        this.corruptRate = corruptRate;
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        OptionParser parser = new OptionParser(args);
        ComputeServerImpl server;
        if (parser.isSafeMode()) {
            server = new ComputeServerImpl(parser.getServerCapacity());
        } else {
            server = new ComputeServerImpl(parser.getServerCapacity(), parser.getCorruptRate());
        }
        server.run(parser);
    }

    @Override
    public Response executeOperation(Operation operation, String login, String password) throws RemoteException {
        if (!this.nameDirectory.authenticateDispatcher(login, password)) {
            return new InvalidCredentialsResponse();
        }
        Random random = new Random();
        float randomNum = random.nextFloat() * 100;
        float refusalRate = this.refusalRate(operation);
        if (refusalRate > 0 && randomNum <= refusalRate) {
            return new OutOfCapacityResponse();
        }
        /*
         * The server always execute the task even if it is corrupted, otherwise
         * the dispatcher would easily be able to spot that this server is corrupted
         */
        int result = operation.execute();
        // If the server is corrupted, return a Response containing a false result with a probability of (corruptRate / 100)
        if (!this.isSafe) {
            randomNum = random.nextFloat() * 100;
            if (randomNum <= this.corruptRate) {
                int falseResult = random.nextInt(4000);
                while (falseResult == result) {
                    falseResult = random.nextInt(4000);
                }
                return new ComputeResponse(falseResult);
            }
        }
        return new ComputeResponse(result);
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
            return ((float)(taskSize - this.serverCapacity) / (float)(4 * this.serverCapacity)) * 100;
        }
    }

    /**
     * Launch the server
     *
     * @param parser The option parser containing information obtained by command line
     */
    public void run(OptionParser parser) {
        try {
            // Get the RMI Registry associated to the directory and lookup the directory stub
            Registry directoryRegistry = LocateRegistry.getRegistry(parser.getDirectoryAddress(), parser.getDirectoryPort());
            NameDirectory nameDirectory = (NameDirectory) directoryRegistry.lookup("NameDirectory");
            this.nameDirectory = nameDirectory;
            nameDirectory.bind(parser.getServerCapacity(), parser.getServerPort());
            // Then a stub is bound to a registry locally
            ComputeServer stub = (ComputeServer) UnicastRemoteObject.exportObject(this,parser.getServerPort() + 1);
            Registry serverRegistry = LocateRegistry.getRegistry(parser.getServerPort());
            serverRegistry.rebind("ComputeServer", stub);
            System.out.println("Compute server ready.");
        } catch (RemoteException e) {
            System.err.println("Remote exception happened during Server creation: ");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.err.println("NotBoundException happened during Server creation: ");
            e.printStackTrace();
            System.exit(1);
        } catch (ServerNotActiveException e) {
            System.err.println("An exception happened during object binding to name directory: ");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
