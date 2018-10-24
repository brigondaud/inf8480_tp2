package com.inf8480_tp2.shared.directory;

import com.inf8480_tp2.shared.server.ComputeServerInterface;

import javax.naming.NamingException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Implements a simple version of LDAP directory for our system using JDNI.
 * This service is also used by the server to authenticate a a dispatcher.
 *
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public interface Directory extends Remote {

    /**
     * Authenticate a given dispatcher
     *
     * @param login The login of the dispatcher to authenticate
     * @param password The password of the dispatcher to authenticate
     * @return A boolean indicating if the dispatcher has been successfully authenticated or not
     */
    boolean authenticateDispatcher(String login, String password) throws RemoteException;

    /**
     * Bind a Java Object to a name using Java Context bind operation
     *
     * @param name The name to bind
     * @param object The Object to bind
     */
    void bindObject(String name, Object object) throws RemoteException, NamingException;

    /**
     * Retrieves a list of all available computing servers' name in the system
     */
    Collection<ComputeServerInterface> getAvailableServers() throws RemoteException, NamingException;

    /**
     * Remove binding for the specified name
     *
     * @param name The name of the Object to unbind
     */
    void unbindObject(String name) throws RemoteException, NamingException;

}
