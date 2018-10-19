package com.inf8480_tp2.server;

import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.operations.Task;
import com.inf8480_tp2.shared.response.Response;
import com.inf8480_tp2.shared.server.ComputeServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class ComputeServer implements ComputeServerInterface {

    private int capacity;

    public ComputeServer(int capacity) {
        this.capacity = capacity;
    }

    public static void main(String[] args) {
        int serverCapacity = Integer.valueOf(args[0]);
        ComputeServer server = new ComputeServer(serverCapacity);
        server.run();
    }

    @Override
    public int getServerCapacity() throws RemoteException {
        return this.capacity;
    }

    @Override
    public Response executeTask(Task task) throws RemoteException {
        // TODO
        return null;
    }

    @Override
    public Response executeOperation(Operation operation) throws RemoteException {
        // TODO
        return null;
    }

    private void run() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = ""; // TODO assign a unique name for every server
            ComputeServerInterface stub = (ComputeServerInterface) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Compute server ready.");
        } catch (Exception e) {
            System.err.println("Server exception");
            e.printStackTrace();
        }
    }

}
