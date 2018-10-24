package com.inf8480_tp2.shared.directory;


import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Implements a simple version of LDAP directory for our system using JDNI.
 * This service is also used by the server to authenticate a a dispatcher.
 *
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class Directory {

    private DirContext directory;

    public Directory() throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");

        this.directory = new InitialDirContext(env);
    }

    /**
     * Authenticate a given dispatcher
     *
     * @param login The login of the dispatcher to authenticate
     * @param password The password of the dispatcher to authenticate
     * @return A boolean indicating if the dispatcher has been successfully authenticated or not
     */
    public boolean authenticateDispatcher(String login, String password) {
        // TODO
        return false;
    }

    /**
     * Bind a Java Object to a name using Java Context bind operation
     *
     * @param name The name to bind
     * @param object The Object to bind
     */
    public void bindObject(String name, Object object) throws NamingException {
        this.directory.bind(name, object);
    }

    /**
     * Retrieves a list of all available computing servers' name in the system
     */
    public void getAvailableServers() throws NamingException {
        // TODO
    }

    /**
     * Retrieves an Object from it has been bound to
     *
     * @param name The bound name of the Object to lookup
     * @return The Object bound to name
     */
    public Object getReference(String name) throws NamingException {
        return this.directory.lookup(name);
    }

    /**
     * Remove binding for the specified name
     *
     * @param name The name of the Object to unbind
     */
    public void unbindObject(String name) throws NamingException {
        this.directory.unbind(name);
    }

}
