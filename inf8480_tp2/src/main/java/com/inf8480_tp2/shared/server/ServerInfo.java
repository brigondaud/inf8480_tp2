package com.inf8480_tp2.shared.server;

/**
 * A class containing all the necessary information about a compute server.
 * Each server will bind it's information through NameDirectory using this class
 *
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class ServerInfo {

    private String ipAddress;
    private int capacity;
    private int port;

    public ServerInfo(String address, int port) {
        this.ipAddress = address;
        this.port = port;
    }

    public ServerInfo(String address, int capacity, int port) {
        this(address, port);
        this.capacity = capacity;
    }

    public String getIpAdress() {
        return ipAddress;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPort() {
        return port;
    }

    public void setIpAdress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServerInfo)) {
            return false;
        }
        ServerInfo other = (ServerInfo) obj;
        return this.ipAddress.equals(other.getIpAdress()) && this.port == other.getPort();
    }

    @Override
    public int hashCode() {
        return this.ipAddress.hashCode() * this.port;
    }

    @Override
    public String toString() {
        String res = "";
        res += "Server IP: " + this.ipAddress + "\n";
        res += "Server port: " + String.valueOf(this.port) + "\n";
        res += "Server capacity: " + String.valueOf(this.capacity) + "\n";
        return res;
    }
}
