package com.inf8480_tp2.directory;


import com.inf8480_tp2.shared.directory.NameDirectory;
import com.inf8480_tp2.shared.server.ServerInfo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Implements a simple LDAP like name directory for our system.
 * This service is also used by the server to authenticate a a dispatcher.
 *
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class NameDirectoryImpl implements NameDirectory {

    private Collection<ServerInfo> serverDirectory;
    private Map<String, String> dispatcherDirectory;

    public NameDirectoryImpl() {
        this.serverDirectory = new HashSet<>();
        this.dispatcherDirectory = new HashMap<>();
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "NameDirectory";
            String hostName = args[0];
            NameDirectory serverDirectory = new NameDirectoryImpl();
            NameDirectory stub = (NameDirectory) UnicastRemoteObject.exportObject(serverDirectory, 0);
            Registry registry = LocateRegistry.getRegistry(hostName);
            registry.rebind(name, stub);
            System.out.println("NameDirectory is ready.");
        } catch (RemoteException e) {
            System.err.println("Error during directory binding in RMI Registry");
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticateDispatcher(String login, String password) {
        return this.dispatcherDirectory.containsKey(login) && this.dispatcherDirectory.get(login).equals(password);
    }

    @Override
    public synchronized void bind(ServerInfo serverInfo) {
        /*
         * If an element with the same IP Address is already present in the collection,
         * it must be remove after the add is performed. Otherwise, nothing will happen and
         * the server info will not be updated
         */
        this.serverDirectory.remove(serverInfo);
        this.serverDirectory.add(serverInfo);
    }

    @Override
    public synchronized Collection<ServerInfo> getAvailableServers() {
        return this.serverDirectory;
    }


    @Override
    public synchronized void registerDispatcher(String login, String password) {
        if (this.dispatcherDirectory.containsKey(login)) {
            // TODO ErrorResponse
        } else {
            this.dispatcherDirectory.put(login, password);
        }
    }

    @Override
    public synchronized void unbind(ServerInfo serverInfo) {
        this.serverDirectory.remove(serverInfo);
    }

}
