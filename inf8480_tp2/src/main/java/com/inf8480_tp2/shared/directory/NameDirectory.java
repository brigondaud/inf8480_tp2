package com.inf8480_tp2.shared.directory;

import com.inf8480_tp2.shared.server.ServerInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
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
     * @param serverCapacity
     * @param serverPort
     */
    void bind(int serverCapacity, int serverPort) throws RemoteException, ServerNotActiveException;

    /**
     * Retrieves a list of all available computing servers references in the system.
     */
    Collection<ServerInfo> getAvailableServers() throws RemoteException;

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
     * @param serverAddress The IP Adress of the server to unbind
     * @param serverPort The port of the server to unbind
     */
    void unbind(String serverAddress, int serverPort) throws RemoteException;

}
