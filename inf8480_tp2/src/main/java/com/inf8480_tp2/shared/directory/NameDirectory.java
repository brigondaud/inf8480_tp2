package com.inf8480_tp2.shared.directory;

import com.inf8480_tp2.shared.server.ComputeServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Implements a simple version of LDAP directory for our system using JDNI.
 * This service is also used by the server to authenticate a a dispatcher.
 *
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public interface NameDirectory extends Remote {

    /**
     * Authenticate a given dispatcher.
     *
     * @param login The login of the dispatcher to authenticate
     * @param password The password of the dispatcher to authenticate
     * @return A boolean indicating if the dispatcher has been successfully authenticated or not
     */
    boolean authenticateDispatcher(String login, String password) throws RemoteException;

    /**
     * Set the specified server available for computations.
     *
     * @param server The server ref to bind
     */
    void bind(ComputeServer server) throws RemoteException;

    /**
     * Retrieves a list of all available computing servers references in the system.
     */
    Collection<ComputeServer> getAvailableServers() throws RemoteException;

    /**
     * Register a Dispatcher in the directory
     *
     * @param login The login of the dispatcher
     * @param password The password of the dispatcher
     */
    void registerDispatcher(String login, String password) throws RemoteException;

    /**
     * Remove availability of a compute server
     *
     * @param server The server ref to unbind
     */
    void unbind(ComputeServer server) throws RemoteException;

}
