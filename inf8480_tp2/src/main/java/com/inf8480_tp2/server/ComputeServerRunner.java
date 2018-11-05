package com.inf8480_tp2.server;

import com.inf8480_tp2.shared.parser.OptionParser;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.ThreadLocalRandom;

/**
 * For tests purpose: start has many compute servers has needed to run tests.
 * The created servers have a random capacity between 1 and 20.
 * 
 * @author Baptiste Rigondaud & Loïc Poncet
 */
public class ComputeServerRunner {
    
    /**
     * Starts safe computation servers.
     * 
     * @param number Number of servers to create.
     * @param dirPort The directory port.
     * @param serverStartingPort The starting port for servers.
     * @throws UnknownHostException 
     */
    public void runSafeServers(int number, int dirPort, int serverStartingPort) throws UnknownHostException {
        for(int i = 0; i < number; i+=1) {
            int serverPort = 2*i+serverStartingPort;
            int capacity = ThreadLocalRandom.current().nextInt(1, 21);
            String[] options = {
                "--ipDir",
                InetAddress.getLocalHost().getHostAddress(),
                "--portDir",
                dirPort+"",
                "--portServer",
                serverPort+"",
                "--capacity",
                capacity+""
            };
            try {
                LocateRegistry.createRegistry(serverPort);
            } catch (RemoteException ex) {
                System.err.println("Server runner: cannot create RMI registry");
            }
            OptionParser parser = new OptionParser(options);
            ComputeServerImpl server = new ComputeServerImpl(parser.getServerCapacity());
            server.run(parser);
        }
    }
    
    /**
     * Starts unsafe computation servers. The malicious rate of each server
     * is random (a float number between 0 and 100).
     * 
     * @param number Number of servers to create.
     * @param dirPort The directory port.
     * @param serverStartingPort The starting port for servers.
     * @throws UnknownHostException 
     */
    public void runUnsafeServers(int number, int dirPort, int serverStartingPort) throws UnknownHostException {
        for(int i = 0; i < number; i+=1) {
            int serverPort = 2*i+serverStartingPort;
            int rate = ThreadLocalRandom.current().nextInt(1, 101);
            int capacity = ThreadLocalRandom.current().nextInt(1, 21);
            String[] options = {
                "--ipDir",
                InetAddress.getLocalHost().getHostAddress(),
                "--portDir",
                dirPort+"",
                "--portServer",
                serverPort+"",
                "--capacity",
                capacity+"",
                "--unsafe",
                "--corrupt",
                rate+""
            };
            try {
                LocateRegistry.createRegistry(serverPort);
            } catch (RemoteException ex) {
                System.err.println("Server runner: cannot create RMI registry");
            }
            OptionParser parser = new OptionParser(options);
            ComputeServerImpl server = new ComputeServerImpl(parser.getServerCapacity(), 
                    parser.getCorruptRate());
            server.run(parser);
        }
    }
    
}
