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
     * Execute an operation on the server. If the server is corrupted, then a random floating point number is
     * generated and if this number is between 0 and the corruption rate of the server, then a false result will be
     * returned.
     *
     * @param operation The operation to execute
     * @param login The login of the dispatcher asking to execute the task
     * @param password The password of the dispatcher asking to execute the task
     * @return A Response object containing the result of the operation, or eventually a false result
     * if the server is corrupted. If the server refused the task, then an OutOfCapacityResponse is returned.
     */
    Response executeOperation(Operation operation, String login, String password) throws RemoteException;

}
