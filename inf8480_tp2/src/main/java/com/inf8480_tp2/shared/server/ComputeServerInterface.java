package com.inf8480_tp2.shared.server;

import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.response.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public interface ComputeServerInterface extends Remote {

    /**
     * Get the amount of mathematical operations the server is able to perform at the moment
     * @return The server's capacity
     */
    int getServerCapacity() throws RemoteException;

    /**
     * Set the amount of mathematical operations the server is able to perform
     *
     * @param capacity The capacity to set
     */
    void setServerCapacity(int capacity) throws RemoteException;

    /**
     * Execute an operation on the server
     * @param operation The operation to execute
     * @return A Response object
     */
    Response executeOperation(Operation operation) throws RemoteException;

}
