package com.inf8480_tp2.server;

import com.inf8480_tp2.directory.ServerDirectory;
import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.response.Response;
import com.inf8480_tp2.shared.server.ComputeServerInterface;

import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 * Implements a server class that will execute some operations assigned by a dispatcher using RMI.
 * Each server has a readable capacity from RMI call.
 *
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class ComputeServer implements ComputeServerInterface {

    private int serverCapacity;

    public ComputeServer(int capacity) {
        this.serverCapacity = capacity;
    }

    public static void main(String[] args) {
        int serverCapacity = Integer.valueOf(args[0]);
        ComputeServer server = new ComputeServer(serverCapacity);
        server.run();
    }

    @Override
    public int getServerCapacity() throws RemoteException {
        return this.serverCapacity;
    }

    @Override
    public void setServerCapacity(int capacity) throws RemoteException {
        this.serverCapacity = capacity;
    }

    @Override
    public Response executeOperation(Operation operation) throws RemoteException {
        Random random = new Random();
        //
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

    private void run() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = ""; // TODO assign a unique name for every server
            ComputeServerInterface stub = (ComputeServerInterface) UnicastRemoteObject.exportObject(this, 0);
            ServerDirectory serverDirectory = new ServerDirectory();
            serverDirectory.bindObject(name, stub);
            System.out.println("Compute server ready.");
        } catch (NamingException namingEx) {
            System.err.println("Naming exception happened");
            namingEx.printStackTrace();
        } catch (Exception e) {
            System.err.println("Server exception");
            e.printStackTrace();
        }
    }

}
