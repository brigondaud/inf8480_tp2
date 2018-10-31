package com.inf8480_tp2.shared.server;

/**
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class ServerInfo {

    private String ipAddress;
    private int capacity;
    private int port;

    public ServerInfo(String ip, int capacity, int port) {
        this.ipAddress = ip;
        this.capacity = capacity;
        this.port = port;
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
        return this.ipAddress.equals(other.ipAddress) && this.port == other.getPort();
    }

    @Override
    public int hashCode() {
        return this.ipAddress.hashCode() * this.port;
    }
}
