package com.inf8480_tp2.shared.directory;


import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Implements a simple version of LDAP directory for our system.
 * This service is also used by the server to authenticate a a dispatcher.
 *
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class Directory {

    private Map<String, String> dispatcherDirectory;

    public Directory() {
        this.dispatcherDirectory = new HashMap<>();
    }

    /**
     * Retrieves a list of all available computing servers in the system
     */
    public void getAvailableServers() {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");

        DirContext dirContext;

        try {
            dirContext = new InitialDirContext(env);
            dirContext.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Authenticate a given dispatcher
     *
     * @param login The login of the dispatcher to authenticate
     * @param password The password of the dispatcher to authenticate
     * @return A boolean indicating if the dispatcher has been successfully authenticated or not
     */
    public boolean authenticate(String login, String password) {
        return this.dispatcherDirectory.containsKey(login) && this.dispatcherDirectory.get(login).equals(password);
    }

}
