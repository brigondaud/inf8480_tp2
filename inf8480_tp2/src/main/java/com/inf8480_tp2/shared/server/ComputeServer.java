package com.inf8480_tp2.shared.server;

import com.inf8480_tp2.shared.operations.Operation;
import com.inf8480_tp2.shared.response.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public interface ComputeServer extends Remote {

    /**
     * Execute an operation on the server
     *
     * @param operation The operation to execute
     * @param login The login of the dispatcher asking to execute the task
     * @param password The password of the dispatcher asking to execute the task
     * @return A Response object
     */
    Response executeOperation(Operation operation, String login, String password) throws RemoteException;

}
