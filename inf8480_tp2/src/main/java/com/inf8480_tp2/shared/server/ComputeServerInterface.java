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
     * Get the amount of work the server is able to perform at the moment
     * @return The server's capacity
     * @throws java.rmi.RemoteException
     */
    int getServerCapacity() throws RemoteException;

    /**
     * Execute an operation on the server
     * @param operation The operation to execute
     * @return A Response object
     * @throws java.rmi.RemoteException
     */
    Response executeOperation(Operation operation) throws RemoteException;

}
