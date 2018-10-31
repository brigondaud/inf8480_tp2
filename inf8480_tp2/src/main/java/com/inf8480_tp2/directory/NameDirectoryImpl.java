package com.inf8480_tp2.directory;


import com.inf8480_tp2.shared.directory.NameDirectory;
import com.inf8480_tp2.shared.server.ComputeServer;

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

    private Collection<ComputeServer> serverDirectory;
    private Map<String, String> dispatcherDirectory;

    public NameDirectoryImpl() {
        this.serverDirectory = new ArrayList<>();
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
    public void bind(ComputeServer server) {
        this.serverDirectory.add(server);
    }

    @Override
    public Collection<ComputeServer> getAvailableServers() {
        return this.serverDirectory;
    }


    @Override
    public void registerDispatcher(String login, String password) {
        // TODO
        this.dispatcherDirectory.put(login, password);
    }

    @Override
    public void unbind(ComputeServer server) {
        this.serverDirectory.remove(server);
    }

}
