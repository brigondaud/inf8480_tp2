package com.inf8480_tp2.directory;


import com.inf8480_tp2.shared.directory.Directory;
import com.inf8480_tp2.shared.server.ComputeServerInterface;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Implements a simple version of LDAP directory for our system using JDNI.
 * This service is also used by the server to authenticate a a dispatcher.
 *
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class ServerDirectory implements Directory {

    private DirContext directory;
    private Map<String, String> dispatcherDirectory;

    public ServerDirectory() throws NamingException {
        this.dispatcherDirectory = new HashMap<>();
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");

        this.directory = new InitialDirContext(env);
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Directory";
            Directory serverDirectory = new ServerDirectory();
            Directory stub = (Directory) UnicastRemoteObject.exportObject(serverDirectory, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ServerDirectory ready.");
        } catch (NamingException e) {
            System.err.println("Error during directory access");
            e.printStackTrace();
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
    public void bindObject(String name, Object object) throws NamingException {
        this.directory.bind(name, object);
    }

    @Override
    public Collection<ComputeServerInterface> getAvailableServers() throws NamingException {
        NamingEnumeration<SearchResult> searchResultEnumeration = this.directory.search("TODO", null);
        Collection<ComputeServerInterface> res = new LinkedList<>();
        while (searchResultEnumeration.hasMore()) {
            SearchResult searchResult = searchResultEnumeration.next();
            ComputeServerInterface computeServer = (ComputeServerInterface) searchResult.getObject();
            res.add(computeServer);
        }
        return res;
    }


    @Override
    public void unbindObject(String name) throws NamingException {
        this.directory.unbind(name);
    }

}
