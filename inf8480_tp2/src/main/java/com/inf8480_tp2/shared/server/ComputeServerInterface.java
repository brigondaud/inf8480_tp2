package com.inf8480_tp2.shared.server;

import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.operations.Task;
import com.inf8480_tp2.shared.response.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public interface ComputeServerInterface extends Remote {

    /**
     * Get the amount of work the server is able to perform at the moment
     * @return The server's capacity
     */
    int getServerCapacity() throws RemoteException;

    /**
     * Execute a given Task
     * @param task The task to execute
     * @return A Response object
     */
    Response executeTask(Task task) throws RemoteException;

    /**
     * Execute a given Operation
     * @param operation The operation to execute
     * @return A Response object
     */
    Response executeOperation(Operation operation) throws  RemoteException;

}
